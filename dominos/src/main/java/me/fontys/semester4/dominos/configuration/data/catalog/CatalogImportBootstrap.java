package me.fontys.semester4.dominos.configuration.data.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class CatalogImportBootstrap {

    @Value("${import.pizzawithingredients}")
    private final String path = "./import-data/pizza_ingredienten*.csv";
    private final ApplicationContext applicationContext;

    @Autowired
    public CatalogImportBootstrap(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean(name = "pizzaWithIngredients")
    public Resource[] getResources() throws IOException {
        return this.applicationContext.getResources(this.path);
    }
}
