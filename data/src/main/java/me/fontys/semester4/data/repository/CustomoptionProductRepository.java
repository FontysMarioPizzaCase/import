package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.CustomoptionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomoptionProductRepository extends JpaRepository<CustomoptionProduct, Void>, JpaSpecificationExecutor<CustomoptionProduct> {

}