package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.OrderCustomOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderCustomOptionRepository extends JpaRepository<OrderCustomOption, Long>, JpaSpecificationExecutor<OrderCustomOption> {

}