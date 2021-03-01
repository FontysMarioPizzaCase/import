CREATE OR REPLACE procedure process_postalcodes()
AS $$
DECLARE
    t_curs cursor for
        select distinct postalcode,starthousenr,endhousenr,even,st.storeid as store from postalcode_import pci
         left join municipality_import mi on mi.municipalityid = pci.municipality
         left join store st on st.municipality like mi."name"
            where
            not exists
            (select from  postalcode_part where postalcode = pci.postalcode
                                            and starthousenr = pci.starthousenr
                                            and endhousenr = pci.endhousenr
                                            and even = pci.even);
    t_row postalcode_import%rowtype;

    idx int8 := COALESCE(null,(select max(postalcodeid) as nr from postalcode_part),0);

begin

    FOR t_row in t_curs loop
            idx = idx + 1;
            insert into postalcode_part (postalcodeid,postalcode,starthousenr,endhousenr,even)
            values (idx,t_row.postalcode,t_row.starthousenr,t_row.endhousenr,t_row.even);
            if t_row.store > 0 then
                insert into postalcode_store (postalcodeid,storeid)
                values (idx,t_row.store);
            end if;
        END LOOP;

    commit;
END;
$$
    LANGUAGE plpgsql;
