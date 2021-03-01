package me.fontys.semester4.tempdata.repository;

import me.fontys.semester4.tempdata.entity.PostalcodeTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostalcodeTempRepository extends JpaRepository<PostalcodeTemp, Long>, JpaSpecificationExecutor<PostalcodeTemp> {


}