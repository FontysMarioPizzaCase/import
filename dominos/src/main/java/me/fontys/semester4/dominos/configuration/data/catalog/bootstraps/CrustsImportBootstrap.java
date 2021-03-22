package me.fontys.semester4.dominos.configuration.data.catalog.bootstraps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class CrustsImportBootstrap {

    @Value("${import.pizzacrusts}")
    private final String path = "./import-data/pizzabodems*.csv";
    private final ApplicationContext applicationContext;

    @Autowired
    public CrustsImportBootstrap(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean(name = "pizzacrusts")
    public Resource[] getResources() throws IOException {
        return this.applicationContext.getResources(this.path);
    }
}
