CREATE OR REPLACE procedure create_addresses()
LANGUAGE plpgsql
AS $$
DECLARE

    address_cursor CURSOR FOR
	SELECT oi.address, c.email, lower(oi.city) as city 
    FROM order_import oi
             LEFT OUTER JOIN customer c ON c.email = LOWER(oi.email)
--             left outer join municipality_import mi on lower(mi.name) = lower(oi.city)
    where oi.address <> '' is true and (c.email <> '') is true;

    address_row RECORD;
    address_count INT = 0;
    created_address_id INT = 0;
    customerid INT = 0;
    postalcode_id INT = 0;
    streetnumber INT = 0;
    streettext VARCHAR;
    lognotice VARCHAR;
    logtime timestamp with time zone;

begin
	logtime = CURRENT_TIMESTAMP;
    OPEN address_cursor;
    LOOP
        FETCH address_cursor INTO address_row;
        EXIT WHEN NOT FOUND;

        BEGIN
	        streetnumber = cast(SUBSTRING(address_row.address, '([0-9]+)') as INTEGER);
	        streettext = trim(SUBSTRING(address_row.address, '^\D+'));
	        -- todo this should not come from municipality name but from store city
	--       if address_row.municipalityid is not null then
	--        select postalcodeid into postalcode_id from postalcode_import pi2 where pi2.municipality = address_row.municipalityid
	--          and lower(pi2.street) = lower(SUBSTRING(address_row.address, '^\D+'))
	--          and pi2.endhousenr > streetnumber
	--          and pi2.starthousenr < streetnumber;
	--          and pi2.even = (streetnumber % 2)::text;
	--         
	--         raise notice 'In if, Found postalcode id %', postalcode_id;
	--		end if;
	--         

	        INSERT INTO address (postalcodeid, streetnr, streetext)
		        VALUES (postalcode_id, streetnumber,streettext) RETURNING address.addressid
	    	    INTO created_address_id;
	
	        SELECT c.customerid
	        	into customerid
		        FROM customer c
		        WHERE c.email = address_row.email;
	
	        INSERT INTO address_customer (customerid, addressid)
	    	    VALUES (created_address_id, customerid);
	        
			address_count = address_count + 1;
	
			EXCEPTION WHEN unique_violation THEN
	            -- Unique violation, should we care to log this? We already have the entry so not important imho
	            -- should check here if the email address is different and log the address to the customer in address_customer
--	            lognotice = 'Duplicate entry found while inserting ' || streettext ||' '|| streetnumber;
--	        	CALL createlogentry(lognotice, logtime);
				CONTINUE;
        END;
      
	END LOOP;

CLOSE address_cursor;

lognotice = 'New addresses: '|| address_count;
call createlogentry(lognotice, logtime);

END $$;
