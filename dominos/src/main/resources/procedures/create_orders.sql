CREATE OR REPLACE procedure create_orders()
    LANGUAGE plpgsql
AS $$
DECLARE

    order_cursor CURSOR FOR SELECT * FROM order_import
        LEFT OUTER JOIN customer c ON c.email = LOWER(order_import.email)
        LEFT OUTER JOIN product p ON LOWER(p.name) = LOWER(order_import.product)
        LEFT OUTER JOIN "order" o ON o.customer = c.customerid
        LEFT OUTER JOIN store s on order_import.store_name = s.name;

    order_row RECORD;
    previous_new_order RECORD;
    ingredient_row RECORD;

    ingredient_item VARCHAR;

    order_count INT = 0;
    orderline_count INT = 0;

    created_order_id INT = 0;
    created_order_product_id INT = 0;

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
            -- process coupon
            CALL process_coupon(created_order_id,order_row.used_coupon);

            INSERT INTO order_product (name, orderid, price, productid, quantity, taxrate)
            VALUES (order_row.product, created_order_id, order_row.product_price, order_row.productid,
                    order_row.quantity, order_row.taxrate)
            RETURNING order_product.order_productid INTO created_order_product_id;

            FOREACH ingredient_item IN ARRAY string_to_array(LOWER(order_row.extra_ingredients), ',')
                LOOP

                    SELECT INTO ingredient_row * FROM ingredient WHERE LOWER(ingredient.name) = TRIM(ingredient_item);

                    IF ingredient_row.ingredientid IS NULL THEN
                        RAISE NOTICE 'Ingredient % does not exist', ingredient_item;
                        CONTINUE WHEN true;
                    END IF;

                    INSERT INTO order_product_ingredient (ingredientid, addprice, name, number, order_productid)
                    VALUES (ingredient_row.ingredientid, 0, ingredient_row.name, 1, created_order_product_id);
                END LOOP;
        ELSE
            INSERT INTO order_product (name, orderid, price, productid, quantity, taxrate)
            VALUES (order_row.product, created_order_id, order_row.product_price, order_row.productid,
                    order_row.quantity, order_row.taxrate)
            RETURNING order_product.order_productid INTO created_order_product_id;

            FOREACH ingredient_item IN ARRAY string_to_array(LOWER(order_row.extra_ingredients), ',')
                LOOP

                    SELECT INTO ingredient_row * FROM ingredient WHERE LOWER(ingredient.name) = TRIM(ingredient_item);

                    IF ingredient_row.ingredientid IS NULL THEN
                        RAISE NOTICE 'Ingredient % does not exist', ingredient_item;
                        CONTINUE WHEN true;
                    END IF;

                    INSERT INTO order_product_ingredient (ingredientid, addprice, name, number, order_productid)
                    VALUES (ingredient_row.ingredientid, 0, ingredient_row.name, 1, created_order_product_id);
                END LOOP;
        END IF;

    END LOOP;

    close order_cursor;

    RAISE NOTICE 'New orders: %', order_count;
    RAISE NOTICE 'New order lines: %', orderline_count;

END $$;