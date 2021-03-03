package me.fontys.semester4.dominos.configuration.data.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class OrderImportBootstrap {

    @Value("${import.order.wildcard}")
    private final String path = "./import-data/order-data-*.csv";
    private final ApplicationContext applicationContext;

    @Autowired
    public OrderImportBootstrap(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean(name = "orders")
    public Resource[] getResources() throws IOException {
        return this.applicationContext.getResources(this.path);
    }
}
