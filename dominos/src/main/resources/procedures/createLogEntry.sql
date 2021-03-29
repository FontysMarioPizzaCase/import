CREATE OR REPLACE procedure createLogEntry(IN msg varchar(255), IN t timestamptz DEFAULT CURRENT_TIMESTAMP)
AS $$
BEGIN
insert into importlog (logentrytime,message)
values (t,msg);
END;
$$
LANGUAGE plpgsql;
