CREATE OR REPLACE procedure createLogEntry(IN msg varchar, IN t timestamptz)
AS $$
BEGIN
    insert into importlog (logentrytime,message)
    values (t,msg);
    commit;
END;
$$
    LANGUAGE plpgsql;
