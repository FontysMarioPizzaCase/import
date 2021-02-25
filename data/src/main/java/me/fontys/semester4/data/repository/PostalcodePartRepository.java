package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.PostalcodePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostalcodePartRepository extends JpaRepository<PostalcodePart, Long>, JpaSpecificationExecutor<PostalcodePart> {

}