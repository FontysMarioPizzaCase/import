CREATE OR REPLACE procedure create_addresses()
LANGUAGE plpgsql
AS $$
DECLARE

    address_cursor CURSOR FOR
    SELECT oi.address, c.email
    FROM order_import oi
             LEFT OUTER JOIN customer c ON c.email = LOWER(oi.email)
             where oi.address <> '' and (c.email <> '') is true;

    address_row RECORD;
    address_count INT = 0;
    created_address_id INT = 0;
    customerid INT = 0;

BEGIN

    OPEN address_cursor;
    LOOP
        FETCH address_cursor INTO address_row;
        EXIT WHEN NOT FOUND;

        BEGIN
        INSERT INTO address (streetnr, streetext)
        VALUES (cast(SUBSTRING(address_row.address, '([0-9]+)') as INTEGER),
            SUBSTRING(address_row.address, '^\D+')) RETURNING address.addressid
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
            RAISE NOTICE 'Duplicate entry found while inserting';
            continue;
        END;

END LOOP;

CLOSE address_cursor;

RAISE NOTICE 'New addresses: %', address_count;

END $$;