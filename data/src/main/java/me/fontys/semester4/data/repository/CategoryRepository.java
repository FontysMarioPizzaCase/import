package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Optional<Category> findByNameIgnoreCase(String name);
}