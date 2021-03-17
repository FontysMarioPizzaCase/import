package me.fontys.semester4.dominos.configuration.data.order.formatter.bootstrap;

import me.fontys.semester4.dominos.configuration.data.order.formatter.OrderDateFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderDateFormatterBootstrap {

    @Bean
    public OrderDateFormatter get() {
        return new OrderDateFormatter();
    }
}
