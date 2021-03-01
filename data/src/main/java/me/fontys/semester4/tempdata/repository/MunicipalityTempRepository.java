package me.fontys.semester4.tempdata.repository;

import me.fontys.semester4.tempdata.entity.MunicipalityTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MunicipalityTempRepository extends JpaRepository<MunicipalityTemp, Long>, JpaSpecificationExecutor<MunicipalityTemp> {


}