package me.fontys.semester4.data.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("Store entity test")
public class StoreTest {

    private Store store;

    @BeforeEach
    public void setup() {
        this.store = new Store(5, "my-name", "my-street");
    }

    @Test
    @DisplayName("Get ID")
    public void testGetId() {
        assertEquals(5, this.store.getStoreId());
    }

    @Test
    @DisplayName("Get name")
    public void testGetName() {
        assertEquals("my-name", this.store.getName());
    }

    @Test
    @DisplayName("Get street")
    public void testGetStreet() {
        assertEquals("my-street", this.store.getStreet());
    }

    @Test
    @DisplayName("Object comparison")
    public void testObjectComparison() {
        Store storeOne = new Store(1, "Dominos Sittard", "Stationsstraat");
        Store storeTwo = new Store(1, "Dominos Sittard", "Stationsstraat");
        Store storeThree = new Store(1, "Dominos Eindhoven", "Karel de Grotelaan");

        assertEquals(storeOne, storeTwo);
        assertEquals(storeOne.hashCode(), storeTwo.hashCode());

        assertNotEquals(storeOne, storeThree);
        assertNotEquals(storeOne.hashCode(), storeThree.hashCode());

        assertNotEquals(storeTwo, storeThree);
        assertNotEquals(storeTwo.hashCode(), storeThree.hashCode());
    }
}
