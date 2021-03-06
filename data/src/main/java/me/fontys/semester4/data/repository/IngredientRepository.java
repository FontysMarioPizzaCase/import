package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>, JpaSpecificationExecutor<Ingredient> {

    Optional<Ingredient> findByNameIgnoreCase(String name);
}