package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.CategoryCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryCouponRepository extends JpaRepository<CategoryCoupon, Void>, JpaSpecificationExecutor<CategoryCoupon> {

}