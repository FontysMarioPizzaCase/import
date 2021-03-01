package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Void>, JpaSpecificationExecutor<CategoryProduct> {

}