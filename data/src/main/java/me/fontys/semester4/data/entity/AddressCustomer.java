package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(AddressCustomer.AddressCustomerID.class)
@Table(name = "address_customer")
public class AddressCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "customerid")
    private Long customerid;

    @Id
    @Column(name = "addressid")
    private Long addressid;

    protected AddressCustomer() {

    }

    public AddressCustomer(Long customerid, Long addressid) {
        this.customerid = customerid;
        this.addressid = addressid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public Long getCustomerid() {
        return customerid;
    }

    public void setAddressid(Long addressid) {
        this.addressid = addressid;
    }

    public Long getAddressid() {
        return addressid;
    }

    @Override
    public String toString() {
        return "AddressCustomer{" +
                "customerid=" + customerid + '\'' +
                "addressid=" + addressid + '\'' +
                '}';
    }

    public static class AddressCustomerID implements Serializable {

        private Long customerid;
        private Long addressid;

        protected AddressCustomerID() {

        }

        public AddressCustomerID(Long customerid, Long addressid) {
            this.customerid = customerid;
            this.addressid = addressid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AddressCustomerID that = (AddressCustomerID) o;
            return Objects.equals(customerid, that.customerid) && Objects.equals(addressid, that.addressid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customerid, addressid);
        }
    }
}
