package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.PostalcodeStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostalcodeStoreRepository extends JpaRepository<PostalcodeStore, Void>, JpaSpecificationExecutor<PostalcodeStore> {

}