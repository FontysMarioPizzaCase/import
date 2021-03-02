package me.fontys.semester4.dominos.configuration.data.order;

import me.fontys.semester4.data.entity.Order;
import me.fontys.semester4.data.repository.OrderCustomOptionRepository;
import me.fontys.semester4.data.repository.OrderProductIngredientRepository;
import me.fontys.semester4.data.repository.OrderProductRepository;
import me.fontys.semester4.data.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Configuration
public class OrderImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderImporter.class);

    private static final int[] ORDER_DELIMITER_COLUMN_IDS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};

    @Qualifier("orders")
    private final Resource[] orders;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderCustomOptionRepository orderCustomOptionRepository;
    private final OrderProductIngredientRepository orderProductIngredientRepository;

    private final Map<String, Integer> warnings = new HashMap<>();

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

    public void doImport() throws IOException {
        LOGGER.info("Starting import of orders...");

        this.warnings.clear();
        List<Order> orders = new ArrayList<>();

        for (Resource resource : this.orders) {
            LOGGER.info("Reading resource " + resource.getFilename());
            orders.addAll(this.processOrderResource(resource));
        }

        LOGGER.info(String.format("Inserting %s orders...", orders.size()));
        this.orderRepository.saveAll(orders);
    }

    public void report() {
        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Order import result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }

    private List<Order> processOrderResource(Resource resource) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

        List<Order> result = new ArrayList<>();

        int rowCount = 0;
        while(reader.ready()) {
            rowCount++;
            String line = reader.readLine();

            // Ignore the initial header rows and empty lines
            if (isHeader(rowCount)) continue;
            if (line.isEmpty()) continue;

            String[] lineDetails = line.split(";");

            if (isDelimitedOrder(lineDetails)) {
                appendDelimitedLineToOrder(lineDetails, result.get(result.size() - 1));
            } else {
                result.add(createNewOrderFromLine(lineDetails));
            }
        }

        return result;
    }

    private boolean isHeader(int rowIndex) {
        return rowIndex <= 5;
    }

    private Order createNewOrderFromLine(String[] line) {
        String storeName = line[0];
        String customerName = line[1];
        String phoneNumber = line[2];
        String email = line[3];
        String address = line[4];
        String city = line[5];
        String orderDate = line[6];
        String deliveryType = line[7];
        String deliveryDate = line[8];
        String deliveryMoment = line[9];
        String productName = line[10];
        String pizzaBottom = line[11];
        String pizzaSauce = line[12];
        String deliveryCost = line[13];
        String quantity = line[14];
        String price = line[15];
        String extraIngredients = line[16];
        String rowPrice = line[17];
        String totalPrice = line[18];
        String usedCoupon = line[19];
        String couponDiscount = line[20];
        String toPayPrice = line[21];

        if (customerName.isEmpty()) {
            processWarning("Order does not have any customer name");
        }

        return new Order(null, null, null, null, null,
                null, deliveryType, totalPrice, deliveryCost, couponDiscount, null, customerName);
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    private void appendDelimitedLineToOrder(String[] line, Order order) {

    }

    private boolean isDelimitedOrder(String[] line) {
        for (int id : ORDER_DELIMITER_COLUMN_IDS) {
            if (id + 1 >= line.length) continue;
            if (!line[id].isEmpty()) return false;
        }
        return true;
    }
}