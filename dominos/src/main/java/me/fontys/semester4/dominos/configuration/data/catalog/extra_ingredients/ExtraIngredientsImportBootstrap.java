package me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class ExtraIngredientsImportBootstrap {

    @Value("${import.ingredientssurcharge}")
    private final String path = "./import-data/ExtraIngredienten*.csv";
    private final ApplicationContext applicationContext;

    @Autowired
    public ExtraIngredientsImportBootstrap(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean(name = "ingredientSurcharge")
    public Resource[] getResources() throws IOException {
        return this.applicationContext.getResources(this.path);
    }
}
