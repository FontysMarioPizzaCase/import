package me.fontys.semester4.dominos.configuration.data.order;

import me.fontys.semester4.data.repository.OrderCustomOptionRepository;
import me.fontys.semester4.data.repository.OrderProductIngredientRepository;
import me.fontys.semester4.data.repository.OrderProductRepository;
import me.fontys.semester4.data.repository.OrderRepository;
import me.fontys.semester4.dominos.DominosImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class OrderImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderImporter.class);

    @Qualifier("orders")
    private final Resource[] orders;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderCustomOptionRepository orderCustomOptionRepository;
    private final OrderProductIngredientRepository orderProductIngredientRepository;

    @Autowired
    public OrderImporter(Resource[] orders, OrderRepository orderRepository,
                         OrderProductRepository orderProductRepository,
                         OrderCustomOptionRepository orderCustomOptionRepository,
                         OrderProductIngredientRepository orderProductIngredientRepository) {
        this.orders = orders;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.orderCustomOptionRepository = orderCustomOptionRepository;
        this.orderProductIngredientRepository = orderProductIngredientRepository;
    }

    public void doImport() {
        LOGGER.info("Starting import of orders...");
        // ...
    }
}
