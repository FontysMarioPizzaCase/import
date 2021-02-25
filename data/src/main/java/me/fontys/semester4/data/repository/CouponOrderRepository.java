package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.CouponOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CouponOrderRepository extends JpaRepository<CouponOrder, Void>, JpaSpecificationExecutor<CouponOrder> {

}