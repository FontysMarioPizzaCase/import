package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.Store;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends CrudRepository<Store, Integer> {

}
