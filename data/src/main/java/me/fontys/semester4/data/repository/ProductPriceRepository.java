package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.stream.Stream;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long>, JpaSpecificationExecutor<ProductPrice> {
    Stream<ProductPrice> findByPriceAndProduct_Productid(BigDecimal price, Long productid);
}