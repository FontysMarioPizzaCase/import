CREATE OR REPLACE procedure createLogEntry(
    IN msg varchar(1000),
    IN t timestamptz DEFAULT CURRENT_TIMESTAMP,
    IN loglevel varchar(12) DEFAULT 'INFO',
    IN logsource varchar(255) DEFAULT 'import system')
AS $$
BEGIN
    insert into importlog (logentrytime,message,logtype,logger)
    values (t,msg,loglevel,logsource);
END;
$$
    LANGUAGE plpgsql;
