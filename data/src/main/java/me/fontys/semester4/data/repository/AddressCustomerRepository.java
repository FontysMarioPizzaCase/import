package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.AddressCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AddressCustomerRepository extends JpaRepository<AddressCustomer, Void>, JpaSpecificationExecutor<AddressCustomer> {

}