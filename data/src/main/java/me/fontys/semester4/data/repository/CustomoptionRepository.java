package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.Customoption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomoptionRepository extends JpaRepository<Customoption, Long>, JpaSpecificationExecutor<Customoption> {

}