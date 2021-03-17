package me.fontys.semester4.dominos.configuration.data.order.formatter.bootstrap;

import me.fontys.semester4.dominos.configuration.data.order.formatter.OrderPhoneNumberFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderPhoneNumberBootstrap {

    @Bean
    public OrderPhoneNumberFormatter create() {
        return new OrderPhoneNumberFormatter();
    }
}
