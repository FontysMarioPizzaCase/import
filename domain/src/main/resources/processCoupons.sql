CREATE OR REPLACE procedure process_coupons(IN logsessiontime timestamptz)
AS
$$
declare
    couponCur cursor for select distinct(oi.used_coupon)
                         from order_import oi
                                  left join coupon c2 on oi.used_coupon = c2.couponcode
                         where oi.used_coupon is not null
                           and oi.used_coupon <> ''
                           and c2.couponcode is null;
    l_context                text;
    percentageAct            integer        := 0;
    eachxFreeAct             integer        := 0;
    fixedpriceAct            numeric(19, 2) := 0;
    minPrice                 numeric(19, 2) := 0;
    fixedpricePart           varchar(40)    := null;
    actionIdToUse            integer        := null;
    conditionIdToUse         integer        := null;
    takeAwayPart             varchar(40)    := null;
    minPricePart             varchar(40)    := null;
    takeAwayConditionToUse   bool           := false;
    takeAwayConditionIdToUse integer        := null;
    newActions               integer        := 0;
    newCouponId              integer        := null;

begin

    for couponTemp in couponCur
        loop
            actionIdToUse = null;
            conditionIdToUse = null;
            takeAwayConditionIdToUse = null;
            newCouponId = null;
            minPricePart = null;
            takeAwayPart = null;
            fixedpricePart = null;
            fixedPriceAct = null;
            minPrice = null;

            -- check if coupon action exist
            if (couponTemp.used_coupon ~ '^.+\% korting op je bestelling$') then

                percentageAct = (REGEXP_MATCHES(couponTemp.used_coupon, '([0-9]+)\%'))[1];
                if ((select count(*)
                     from coupon_actions
                     where d_percentage = percentageAct) = 0)
                then
                    insert into coupon_actions (d_percentage)
                    values (percentageAct)
                    returning actionid into actionIdToUse;
                    newActions = newActions + 1;
                    call createLogEntry(format('created coupon action percentage %L%% reduction id:%L',
                                               percentageAct, actionIdToUse), logsessiontime,'INFO','process_coupons procedure');
                else
                    actionIdToUse = (select ca.actionid
                                     from coupon_actions ca
                                     where ca.d_percentage = percentageAct
                                       and ca.each_x_free is null
                                       and ca.fixedprice is null
                                       and ca.freeship is null);
                end if;
            end if;

            -- check if coupon action exist
            if (couponTemp.used_coupon ~ '^[1-9]+e pizza gratis bij .+$') then

                eachxFreeAct = (REGEXP_MATCHES(couponTemp.used_coupon, '^([1-9]+)e'))[1];
                if ((select count(*)
                     from coupon_actions ca
                     where ca.each_x_free = eachxFreeAct
                       and ca.d_percentage is null
                       and ca.fixedprice is null
                       and ca.freeship is null) = 0)
                then
                    insert into coupon_actions (each_x_free)
                    values (eachxFreeAct)
                    returning actionid into actionIdToUse;
                    newActions = newActions + 1;

                    call createLogEntry(format('created coupon action each %L free id:%L',
                                               eachxFreeAct, actionIdToUse), logsessiontime,'INFO','process_coupons procedure');
                else
                    actionIdToUse = (select ca.actionid
                                     from coupon_actions ca
                                     where ca.each_x_free = eachxFreeAct
                                       and ca.d_percentage is null
                                       and ca.fixedprice is null
                                       and ca.freeship is null);
                end if;
            end if;

            -- check if coupon action exist
            if (couponTemp.used_coupon ~ '.+ [0-9\,\-]+ Korting op.+$') then
                fixedpricePart = (REGEXP_MATCHES(couponTemp.used_coupon, '.+ ([0-9\,\-]+) Korting'))[1];
                call createLogEntry(format('extracted coupon action fixedprice %L', fixedpricePart)::varchar(255),
                                    logsessiontime,'INFO','process_coupons procedure');
                fixedpriceAct = TO_NUMBER(replace(replace(fixedpricePart, ',', '.'), '-', '00'), '999D9S');

                if ((select count(*)
                     from coupon_actions ca
                     where ca.fixedprice = fixedpriceAct
                       and ca.d_percentage is null
                       and ca.each_x_free is null
                       and ca.freeship is null) = 0)
                then
                    insert into coupon_actions (fixedprice)
                    values (fixedpriceAct)
                    returning actionid into actionIdToUse;
                    newActions = newActions + 1;
                    call createLogEntry(format('created coupon action reduce price by %L id:%L', fixedpriceAct,
                                               actionIdToUse), logsessiontime,'INFO','process_coupons procedure');
                else
                    actionIdToUse = (select ca.actionid
                                     from coupon_actions ca
                                     where ca.fixedprice = fixedpriceAct
                                       and ca.d_percentage is null
                                       and ca.each_x_free is null
                                       and ca.freeship is null);
                end if;
            end if;

            -- check if coupon condition exists
            if (couponTemp.used_coupon ~ '(afhalen|bezorgen|vanaf)') then
                takeAwayPart = (REGEXP_MATCHES(couponTemp.used_coupon, '(afhalen|bezorgen)'))[1];
                if (takeAwayPart = 'afhalen')
                then
                    takeAwayConditionToUse = true;
                else
                    if (takeAwayPart = 'bezorgen') then
                        takeAwayConditionToUse = false;
                    else
                        takeAwayConditionToUse = null;
                    end if;
                end if;
                if (couponTemp.used_coupon ~ '(vanaf)') then
                    minPricePart = (REGEXP_MATCHES(couponTemp.used_coupon, 'vanaf . ([0-9\,\-]+)'))[1];
                    call createLogEntry(format('extracted coupon condition minimum price %L', minPricePart),
                                        logsessiontime,'INFO','process_coupons procedure');
                    minPrice = TO_NUMBER(replace(replace(minPricePart, ',', '.'), '-', '00'), '999D9S');
                else
                    minPrice = null;
                end if;
                if ((select count(*)
                     from coupon_conditions cc
                     where cc.takeaway = takeAwayConditionToUse
                       and cc.min_price = minPrice
                       and cc.min_quantity is null) = 0)
                then
                    insert into coupon_conditions (takeaway, min_price)
                    values (takeAwayConditionToUse, minPrice)
                    returning conditionid into takeAwayConditionIdToUse;
                    newActions = newActions + 1;
                    call createLogEntry(format('created coupon condition takeaway:"%L" minprice:"%L" id:%L',
                                               takeAwayConditionToUse, minPrice, takeAwayConditionIdToUse),
                                        logsessiontime,'INFO','process_coupons procedure');
                else
                    takeAwayConditionIdToUse = (select cc.conditionid
                                                from coupon_conditions cc
                                                where cc.takeaway = takeAwayConditionToUse
                                                  and cc.min_price = minPrice
                                                  and cc.min_quantity is null);
                end if;
            end if;
            --create new coupon and associate with correct action and condition (if applicable)
            insert into coupon (couponcode, "action", "condition")
            values (couponTemp.used_coupon, actionIdToUse, takeAwayConditionIdToUse)
            returning couponid into newCouponId;
            call createLogEntry(format('created coupon with code "%L" actionid:%L conditionid:"%L" couponid:%L',
                                       couponTemp.used_coupon, actionIdToUse, takeAwayConditionIdToUse, newCouponId),
                                logsessiontime,'INFO','process_coupons procedure');

        end loop;
exception
    when others then
        GET STACKED DIAGNOSTICS l_context = PG_EXCEPTION_CONTEXT;
        call createLogEntry(format('%L', right(l_context, 1000)), logsessiontime,'ERROR','process_coupons procedure');
        raise;
end;
$$
    LANGUAGE plpgsql;