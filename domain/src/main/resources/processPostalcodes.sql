CREATE OR REPLACE procedure process_postalcodes(IN logsessiontime timestamptz)
AS $$
DECLARE
    t_curs cursor for
        select distinct pci.postalcode,pci.starthousenr,pci.endhousenr,pci.even,pci.street,st.storeid as store from postalcode_import pci
            left join municipality_import mi on mi.municipalityid = pci.municipality
            left join store st on st.municipality like mi."name"
        where
            not exists
                (select from  postalcode_part where postalcode = pci.postalcode
                                                and starthousenr = pci.starthousenr
                                                and endhousenr = pci.endhousenr
                                                and even = pci.even
                                                and street = pci.street);
    t_row postalcode_import%rowtype;

    idx int8 := COALESCE(null,(select max(postalcodeid) as nr from postalcode_part),0);

    nrImportedPC int8 := 0;
    nrImportedStoreRel int8 := 0;

    result_curs cursor for
        select distinct pci.postalcode from public.postalcode_part pci;
    r_row public.postalcode_import%rowtype;

    nrUniquePC int8 := 0;

begin

    FOR t_row in t_curs loop
            idx = idx + 1;
            nrImportedPC = nrImportedPC + 1;
            insert into postalcode_part (postalcodeid,postalcode,starthousenr,endhousenr,even,street)
            values (idx,t_row.postalcode,t_row.starthousenr,t_row.endhousenr,t_row.even,t_row.street);
            if t_row.store > 0 then
                insert into postalcode_store (postalcodeid,storeid)
                values (idx,t_row.store);
                nrImportedStoreRel = nrImportedStoreRel + 1;
            end if;
        END LOOP;

    call createLogEntry(('Imported postalcode ranges : ' || nrImportedPC), logsessiontime);
    call createLogEntry(('Imported available store ranges : ' || nrImportedStoreRel), logsessiontime);
    for r_row in result_curs loop
        nrUniquePC = nrUniquePC + 1;
        end loop;
    call createLogEntry(('Unique postal codes : ' || nrUniquePC), logsessiontime);

    commit;
END;
$$
    LANGUAGE plpgsql;