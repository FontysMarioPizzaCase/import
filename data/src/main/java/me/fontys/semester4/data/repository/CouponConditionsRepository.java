package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.CouponConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CouponConditionsRepository extends JpaRepository<CouponConditions, Long>, JpaSpecificationExecutor<CouponConditions> {

}