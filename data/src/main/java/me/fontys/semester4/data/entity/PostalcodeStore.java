package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(PostalcodeStore.PostalcodeStoreID.class)
@Table(name = "postalcode_store")
public class PostalcodeStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "storeid")
    private Long storeid;

    @Id
    @Column(name = "postalcodeid")
    private Long postalcodeid;

    protected PostalcodeStore() {

    }

    public PostalcodeStore(Long storeid, Long postalcodeid) {
        this.storeid = storeid;
        this.postalcodeid = postalcodeid;
    }

    public void setStoreid(Long storeid) {
        this.storeid = storeid;
    }

    public Long getStoreid() {
        return storeid;
    }

    public void setPostalcodeid(Long postalcodeid) {
        this.postalcodeid = postalcodeid;
    }

    public Long getPostalcodeid() {
        return postalcodeid;
    }

    @Override
    public String toString() {
        return "PostalcodeStore{" +
                "storeid=" + storeid + '\'' +
                "postalcodeid=" + postalcodeid + '\'' +
                '}';
    }

    public static class PostalcodeStoreID implements Serializable {

        private Long storeid;
        private Long postalcodeid;

        protected PostalcodeStoreID() {

        }

        public PostalcodeStoreID(Long storeid, Long postalcodeid) {
            this.storeid = storeid;
            this.postalcodeid = postalcodeid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PostalcodeStoreID that = (PostalcodeStoreID) o;
            return Objects.equals(storeid, that.storeid) && Objects.equals(postalcodeid, that.postalcodeid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(storeid, postalcodeid);
        }
    }
}
