CREATE OR REPLACE FUNCTION cleanup(_tb text)
RETURNS VOID AS $$
DECLARE
BEGIN
    EXECUTE TRUNCATE TABLE _tb;
END $$ LANGUAGE plpgsql;