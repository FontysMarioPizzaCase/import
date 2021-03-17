package me.fontys.semester4.tempdata.repository;

import me.fontys.semester4.tempdata.entity.OrderTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderTempRepository extends JpaRepository<OrderTemp, Long>, JpaSpecificationExecutor<OrderTemp> {

}