package me.fontys.semester4.dominos.configuration.data.order.test;

import me.fontys.semester4.data.repository.OrderCustomOptionRepository;
import me.fontys.semester4.data.repository.OrderProductIngredientRepository;
import me.fontys.semester4.data.repository.OrderProductRepository;
import me.fontys.semester4.data.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.Preconditions;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Order import record count")
public class OrderImportRecordCountTest {

    public static OrderRepository orderRepository;
    public static OrderProductRepository orderProductRepository;
    public static OrderCustomOptionRepository orderCustomOptionRepository;
    public static OrderProductIngredientRepository orderProductIngredientRepository;

    @BeforeEach
    public void preconditions() {
        Preconditions.notNull(orderRepository, "orderRepository repository may not be null");
        Preconditions.notNull(orderProductRepository, "orderProductRepository repository may not be null");
        Preconditions.notNull(orderCustomOptionRepository, "orderCustomOptionRepository repository may not be null");
        Preconditions.notNull(orderProductIngredientRepository, "orderProductIngredientRepository repository may not be null");
    }

    @Test
    @DisplayName("Check imported row count")
    public void checkImportedRowCount() {
        assertEquals(40000, orderRepository.findAll().size());
    }
}
