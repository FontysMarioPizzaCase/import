package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.IngredientProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IngredientProductRepository extends JpaRepository<IngredientProduct, Void>, JpaSpecificationExecutor<IngredientProduct> {

}