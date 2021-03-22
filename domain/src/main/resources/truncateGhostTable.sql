CREATE OR REPLACE PROCEDURE truncate_if_exists(tablename text)
language plpgsql
as $$
begin
    perform 1
    from information_schema.tables
    where table_name = tablename;
    if found then
        execute format('truncate %I', tablename);
end if;
end $$;