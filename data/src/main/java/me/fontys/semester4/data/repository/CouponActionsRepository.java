package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.CouponActions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CouponActionsRepository extends JpaRepository<CouponActions, Long>, JpaSpecificationExecutor<CouponActions> {

}