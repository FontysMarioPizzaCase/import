package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.util.Streamable;

import java.util.Optional;
import java.util.stream.Stream;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long>, JpaSpecificationExecutor<ProductPrice> {
    Stream<ProductPrice> findByPriceAndProduct_Productid(String price, Long productid);
    boolean existsByPriceAndProduct_Productid(String price, Long productid);
}