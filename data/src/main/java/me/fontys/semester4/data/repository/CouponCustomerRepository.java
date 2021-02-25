package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.CouponCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CouponCustomerRepository extends JpaRepository<CouponCustomer, Void>, JpaSpecificationExecutor<CouponCustomer> {

}