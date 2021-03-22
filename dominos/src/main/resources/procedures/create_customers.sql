CREATE OR REPLACE procedure create_customers()
    LANGUAGE plpgsql
AS
$$
DECLARE

    customer_cursor CURSOR FOR SELECT DISTINCT ON (lower(order_import.email)) order_import.email as email,
                                                                              order_import.customer_name,
                                                                              c.email
                               FROM public.order_import
                                        LEFT OUTER JOIN customer c on lower(order_import.email) = c.email
                               WHERE order_import.email <> ''
                                 AND c.email IS NULL
                               GROUP BY order_import.email, order_import.customer_name, c.email;
    customer_row   RECORD;
    customer_count INT = 0;

BEGIN

    OPEN customer_cursor;

    LOOP
        FETCH customer_cursor INTO customer_row;
        EXIT WHEN NOT FOUND;

        -- TRY CATCH?
        INSERT INTO customer (email, lastname, name)
        VALUES (LOWER(customer_row.email),
                SUBSTRING(customer_row.customer_name, '^\S+'),
                SUBSTRING(customer_row.customer_name, '^\S+\s+(.*)'));

        customer_count = customer_count + 1;
    END LOOP;

    CLOSE customer_cursor;

    RAISE NOTICE 'New customers: %', customer_count;

END
$$;