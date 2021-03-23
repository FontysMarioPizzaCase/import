CREATE OR REPLACE procedure create_orders()
    LANGUAGE plpgsql
AS
$$
DECLARE

    order_cursor CURSOR FOR SELECT *
                            FROM order_import
                                     LEFT OUTER JOIN customer c ON c.email = LOWER(order_import.email)
                                     LEFT OUTER JOIN product p ON LOWER(p.name) = LOWER(order_import.product)
                                     LEFT OUTER JOIN "order" o ON o.customer = c.customerid
                                     LEFT OUTER JOIN store s on order_import.store_name = s.name;
    order_row          RECORD;
    previous_new_order RECORD;
    order_count        INT = 0;
    orderline_count    INT = 0;
    created_order_id   INT = 0;

BEGIN

    OPEN order_cursor;

    LOOP
        FETCH order_cursor INTO order_row;
        EXIT WHEN NOT FOUND;

        IF order_row.productid IS NULL THEN
            RAISE NOTICE 'Order did not have valid product';
            CONTINUE WHEN true;
        END IF;

        IF order_row.store_name <> '' AND order_row.customerid IS NOT NULL THEN

            previous_new_order = order_row;

            INSERT INTO "order" (applied_discount, customer, customername, deliverydate, deliveryprice, orderdate,
                                 postalcodeid, streetnr, takeaway, totalprice, store)
            VALUES (null, order_row.customerid, order_row.customer_name, order_row.delivery_time,
                    order_row.delivery_cost, order_row.order_date, 1, 1, LOWER(order_row.delivery_type) = 'afhalen',
                    order_row.total_price, order_row.storeid)
            RETURNING "order".orderid INTO created_order_id;

            INSERT INTO public.order_product (name, orderid, price, productid, quantity, taxrate)
            VALUES (order_row.product, created_order_id, order_row.product_price, order_row.productid,
                    order_row.quantity, order_row.taxrate);

            order_count = order_count + 1;
            orderline_count = orderline_count + 1;
        ELSE
            INSERT INTO public.order_product (name, orderid, price, productid, quantity, taxrate)
            VALUES (order_row.product, created_order_id, order_row.product_price, order_row.productid,
                    order_row.quantity, order_row.taxrate);

            orderline_count = orderline_count + 1;
        END IF;

    END LOOP;

    close order_cursor;

    RAISE NOTICE 'New orders: %', order_count;
    RAISE NOTICE 'New order lines: %', orderline_count;

END
$$;