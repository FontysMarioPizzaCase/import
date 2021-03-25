CREATE OR REPLACE procedure process_coupons(IN logsessiontime timestamptz)

AS $$
declare
    couponCur cursor for select distinct(oi.used_coupon) from order_import oi
                         left join coupon c2 on oi.used_coupon = c2.couponcode
                         where oi.used_coupon is not null
                           and oi.used_coupon <> ''
                           and c2.couponcode is null;

    percentageAct integer := 0;
    eachxFreeAct integer := 0;
    fixedpriceAct numeric(19,2) := 0;
    minPrice numeric(19,2) := 0;
    fixedpricePart varchar(40) := null;
    actionIdToUse integer := null;
    conditionIdToUse integer := null;
    takeAwayPart varchar(40) := null;
    minPricePart varchar(40) := null;
    takeAwayConditionToUse bool := false;
    takeAwayConditionIdToUse integer := null;
    newActions integer := 0;
    newCouponId integer := null;

    dynCur refcursor;

begin

    for couponTemp in couponCur loop
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
            if(couponTemp.used_coupon ~ '^.+\% korting op je bestelling$') then

                percentageAct = (REGEXP_MATCHES(couponTemp.used_coupon,'([0-9]+)\%'))[1];
                if( (select count(*) from coupon_actions
                     where d_percentage = percentageAct) = 0)
                then
                    insert into coupon_actions (d_percentage)
                    values (percentageAct)
                    returning actionid into actionIdToUse;
                    newActions = newActions + 1;
                    call createLogEntry(('created coupon action percentage ' || percentageAct ||
                                         '% reduction id:' || actionIdToUse), logsessiontime);
                else
                    actionIdToUse = (select ca.actionid from coupon_actions ca
                                     where ca.d_percentage = percentageAct
                                       and ca.each_x_free is null
                                       and ca.fixedprice is null
                                       and ca.freeship is null);
                end if;
            end if;

-- check if coupon action exist
            if(couponTemp.used_coupon ~ '^[1-9]+e pizza gratis bij .+$') then

                eachxFreeAct = (REGEXP_MATCHES(couponTemp.used_coupon,'^([1-9]+)e'))[1];
                if( (select count(*) from coupon_actions ca
                     where ca.each_x_free = eachxFreeAct
                       and ca.d_percentage is null
                       and ca.fixedprice is null
                       and ca.freeship is null) = 0)
                then
                    insert into coupon_actions (each_x_free)
                    values (eachxFreeAct)
                    returning actionid into actionIdToUse;
                    newActions = newActions + 1;

                    call createLogEntry(('created coupon action each ' || eachxFreeAct ||
                                         ' free id:' || actionIdToUse), logsessiontime);
                else
                    actionIdToUse = (select ca.actionid from coupon_actions ca
                                     where ca.each_x_free = eachxFreeAct
                                       and ca.d_percentage is null
                                       and ca.fixedprice is null
                                       and ca.freeship is null);
                end if;
            end if;

-- check if coupon action exist
            if(couponTemp.used_coupon ~ '.+ [0-9\,\-]+ Korting op.+$') then
                fixedpricePart = (REGEXP_MATCHES(couponTemp.used_coupon,'.+ ([0-9\,\-]+) Korting'))[1];
                call createLogEntry(('extracted coupon action fixedprice ' || fixedpricePart), logsessiontime);
                fixedpriceAct = TO_NUMBER(replace(replace(fixedpricePart,',','.'),'-','00'),'999D9S');

                if( (select count(*) from coupon_actions ca
                     where ca.fixedprice = fixedpriceAct
                       and ca.d_percentage is null
                       and ca.each_x_free is null
                       and ca.freeship is null) = 0)
                then
                    insert into coupon_actions (fixedprice)
                    values (fixedpriceAct)
                    returning actionid into actionIdToUse;
                    newActions = newActions + 1;
                    call createLogEntry(('created coupon action reduce price by ' || fixedpriceAct), logsessiontime);
                else
                    actionIdToUse = (select ca.actionid from coupon_actions ca
                                     where ca.fixedprice = fixedpriceAct
                                       and ca.d_percentage is null
                                       and ca.each_x_free is null
                                       and ca.freeship is null);
                end if;
            end if;

-- check if coupon condition exists
            if(couponTemp.used_coupon ~ '(afhalen|bezorgen|vanaf)') then
                takeAwayPart = (REGEXP_MATCHES(couponTemp.used_coupon,'(afhalen|bezorgen)'))[1];
                if(takeAwayPart = 'afhalen')
                then
                    takeAwayConditionToUse = true;
                else
                    if(takeAwayPart = 'bezorgen') then
                        takeAwayConditionToUse = false;
                    else
                        takeAwayConditionToUse = null;
                    end if;
                end if;
                if(couponTemp.used_coupon ~ '(vanaf)') then
                    minPricePart = (REGEXP_MATCHES(couponTemp.used_coupon,'vanaf . ([0-9\,\-]+)'))[1];
                    call createLogEntry(('extracted coupon condition minimum price ' || minPricePart), logsessiontime);
                    minPrice = TO_NUMBER(replace(replace(minPricePart,',','.'),'-','00'),'999D9S');
                else
                    minPrice = null;
                end if;
                if( (select count(*) from coupon_conditions cc
                     where cc.takeaway = takeAwayConditionToUse
                       and cc.min_price = minPrice
                       and cc.min_quantity is null) = 0)
                then
                    insert into coupon_conditions (takeaway,min_price)
                    values (takeAwayConditionToUse,minPrice)
                    returning conditionid into takeAwayConditionIdToUse;
                    newActions = newActions + 1;
                    call createLogEntry(('created coupon condition takeaway:"' || takeAwayConditionToUse ||
                                         '" minprice:"'|| minPrice ||'" id:' || takeAwayConditionIdToUse), logsessiontime);
                else
                    takeAwayConditionIdToUse = (select cc.conditionid from coupon_conditions cc
                                                where cc.takeaway = takeAwayConditionToUse
                                                  and cc.min_price = minPrice
                                                  and cc.min_quantity is null);
                end if;
            end if;

            insert into coupon (couponcode, "action", "condition")
            values (couponTemp.used_coupon,actionIdToUse,takeAwayConditionIdToUse)
            returning couponid into newCouponId;
            call createLogEntry(('created coupon with code "'|| couponTemp.used_coupon ||
                                 '" actionid:' || actionIdToUse ||
                                 ' conditionid:"'|| takeAwayConditionIdToUse ||'" couponid:' || newCouponId), logsessiontime);

        end loop;

end
$$
    LANGUAGE plpgsql;