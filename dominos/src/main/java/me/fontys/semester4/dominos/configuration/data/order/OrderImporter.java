package me.fontys.semester4.dominos.configuration.data.order;

import me.fontys.semester4.data.entity.Order;
import me.fontys.semester4.data.entity.Customer;
import me.fontys.semester4.data.repository.OrderCustomOptionRepository;
import me.fontys.semester4.data.repository.OrderProductIngredientRepository;
import me.fontys.semester4.data.repository.OrderProductRepository;
import me.fontys.semester4.data.repository.OrderRepository;
import me.fontys.semester4.data.repository.CustomerRepository;
import me.fontys.semester4.dominos.configuration.data.ImportTest;
import me.fontys.semester4.dominos.configuration.data.order.formatter.OrderDateFormatter;
import me.fontys.semester4.dominos.configuration.data.order.formatter.OrderPhoneNumberFormatter;
import me.fontys.semester4.dominos.configuration.data.order.test.OrderImportRecordValidityTest;
import me.fontys.semester4.tempdata.entity.OrderTemp;
import me.fontys.semester4.tempdata.repository.OrderTempRepository;
import me.fontys.semester4.utils.StoredProcedureExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final OrderTempRepository orderTempRepository;
    private final CustomerRepository customerRepository;
    private final OrderDateFormatter orderDateFormatter;
    private final OrderPhoneNumberFormatter orderPhoneNumberFormatter;
    private final StoredProcedureExecutor storedProcedureExecutor;
    private final Map<String, Integer> warnings = new HashMap<>();
    private final Resource createCustomerStoredProcedure;

    @Autowired
    public OrderImporter(Resource[] orders, OrderRepository orderRepository,
                         OrderProductRepository orderProductRepository,
                         OrderCustomOptionRepository orderCustomOptionRepository,
                         OrderProductIngredientRepository orderProductIngredientRepository,
                         OrderTempRepository orderTempRepository, OrderDateFormatter orderDateFormatter,
                         CustomerRepository customerRepository, OrderPhoneNumberFormatter orderPhoneNumberFormatter,
                         StoredProcedureExecutor storedProcedureExecutor,
                         @Value("classpath:procedures/create_customers.sql") Resource createCustomerStoredProcedure) {
        this.orders = orders;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.orderCustomOptionRepository = orderCustomOptionRepository;
        this.orderProductIngredientRepository = orderProductIngredientRepository;
        this.orderTempRepository = orderTempRepository;
        this.orderDateFormatter = orderDateFormatter;
        this.customerRepository = customerRepository;
        this.orderPhoneNumberFormatter = orderPhoneNumberFormatter;
        this.storedProcedureExecutor = storedProcedureExecutor;
        this.createCustomerStoredProcedure = createCustomerStoredProcedure;
    }

    public void doImport() throws IOException, SQLException {
        LOGGER.info("Starting import of orders and customers...");

        this.createOrUpdateProcedures();

        this.warnings.clear();
        List<OrderTemp> orders = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        for (Resource resource : this.orders) {
            LOGGER.info("Reading resource " + resource.getFilename());
            orders.addAll(this.processOrderResource(resource));
            customers.addAll(this.processCustomerResource(resource));
        }

        LOGGER.info(String.format("Inserting %s orders...", orders.size()));
        this.orderTempRepository.saveAll(orders);

        LOGGER.info(String.format("Inserting %s customers...", customers.size()));
        this.customerRepository.saveAll(customers);
        this.storedProcedureExecutor.executeSql("CALL create_customers()", false);
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

    public void test() {
        LOGGER.info("Running test assertions...");

        // Setup test dependencies, bit dirty but it works...
        OrderImportRecordValidityTest.orderRepository = orderRepository;
        OrderImportRecordValidityTest.orderProductRepository = orderProductRepository;
        OrderImportRecordValidityTest.orderCustomOptionRepository = orderCustomOptionRepository;
        OrderImportRecordValidityTest.orderProductIngredientRepository = orderProductIngredientRepository;

        ImportTest.test("Order", OrderImportRecordValidityTest.class);
    }

    private void createOrUpdateProcedures() throws IOException, SQLException {
        this.storedProcedureExecutor.createOrReplaceStoredProcedure(this.createCustomerStoredProcedure);
    }

    private List<OrderTemp> processOrderResource(Resource resource) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

        List<OrderTemp> result = new ArrayList<>();

        int rowCount = 0;
        while(reader.ready()) {
            rowCount++;
            String line = reader.readLine();

            // Ignore the initial header rows and empty lines
            if (isHeader(rowCount)) continue;
            if (line.isEmpty()) continue;

            String[] lineDetails = line.split(";");

            OrderTemp order = createNewOrderFromLine(lineDetails);
            if (order != null) {
                result.add(order);
            }
        }

        return result;
    }

    private List<Customer> processCustomerResource(Resource resource) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        List<Customer> result = new ArrayList<>();
        int rowCount = 0;
        while (reader.ready()) {
            rowCount++;
            String line = reader.readLine();
            // Ignore the initial header rows and empty lines
            if (isHeader(rowCount)) continue;
            if (line.isEmpty()) continue;

            String[] lineDetails = line.split(";");

            if (!isDelimitedOrder(lineDetails)) {
                Customer customer = getCustomerFromLine(lineDetails);
                if (customer == null) continue;

                result.add(customer);
            }
        }
        return result;
    }

    private boolean isHeader(int rowIndex) {
        return rowIndex <= 5;
    }

    private OrderTemp createNewOrderFromLine(String[] line) {

        if (line.length < 19) return null;

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
        String pizzaPrice = line[13];
        String deliveryCost = line[14];
        String quantity = line[15];
        String extraIngredients = line[16];
        String extraIngredientPrice = line[17];
        String rowPrice = line[18];
        String totalPrice = null;
        String usedCoupon = null;
        String couponDiscount = null;
        String toPayPrice = null;

        if (line.length > 19) {
            totalPrice = line[19];
            usedCoupon = line[20];
            couponDiscount = line[21];
            toPayPrice = line[22];
        }

        return new OrderTemp(storeName, customerName, this.orderPhoneNumberFormatter.fromString(phoneNumber), email, address, city,
                this.orderDateFormatter.fromString(orderDate), deliveryType,
                this.orderDateFormatter.fromString(deliveryDate + " " + deliveryMoment), productName, pizzaBottom,
                pizzaSauce, parsePrice(pizzaPrice), parsePrice(deliveryCost), parseQuantity(quantity), extraIngredients,
                parsePrice(extraIngredientPrice), parsePrice(rowPrice), parsePrice(totalPrice), usedCoupon,
                parsePrice(couponDiscount), parsePrice(toPayPrice));
    }

    private BigDecimal parsePrice(String price) {
        if (price == null || price.isEmpty()) return null;

        price = price
                .replace("€", "")
                .replace(",", ".")
                .trim();

        if (price.isEmpty()) return null;

        return new BigDecimal(price);
    }

    private int parseQuantity(String quantity) {
        if (quantity == null || quantity.isEmpty()) return 0;

        quantity = quantity.trim();

        return Integer.parseInt(quantity);
    }

    private Customer getCustomerFromLine(String[] line) {

        // If it doesn't have at least 3 fields, we can't build a customer
        if (line.length < 8) return null;

        String customerName = line[1];

        String[] customerNameSplit = customerName.split(" ");
        String firstName = customerNameSplit[0];
        StringBuilder lastName = new StringBuilder();

        for (int i = 0; i < customerNameSplit.length; i++) {
            if (i == 0) continue;
            lastName.append(" ").append(customerNameSplit[i]);
        }

        String email = line[3].toLowerCase();
        String deliveryType = line[7];

        if ("bezorgen".equalsIgnoreCase(deliveryType) && customerName.isEmpty()) {
            processWarning("Order does not have any customer name");
        }
        if ("bezorgen".equalsIgnoreCase(deliveryType) && email.isEmpty()) {
            processWarning("Order does not have any email");
        }

        if ("afhalen".equalsIgnoreCase(deliveryType) && (customerName.isEmpty() || email == null || email.isEmpty())) {

            // It was delivery, and we have no customer name & email. We can skip this
            return null;
        }

        if (this.customerRepository.findByEmail(email).isEmpty()) {
            // we already have customer based on email
            return null;
        }

        return new Customer(null, email, firstName, lastName.toString());
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
