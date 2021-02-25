package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.OrderProductIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderProductIngredientRepository extends JpaRepository<OrderProductIngredient, Long>, JpaSpecificationExecutor<OrderProductIngredient> {

}