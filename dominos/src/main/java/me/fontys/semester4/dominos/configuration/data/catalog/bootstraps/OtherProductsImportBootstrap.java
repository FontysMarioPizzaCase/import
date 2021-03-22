package me.fontys.semester4.dominos.configuration.data.catalog.bootstraps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class OtherProductsImportBootstrap {

    @Value("${import.otherproducts}")
    private final String path = "./import-data/Overige_producten*.csv";
    private final ApplicationContext applicationContext;

    @Autowired
    public OtherProductsImportBootstrap(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean(name = "overigeProducten")
    public Resource[] getResources() throws IOException {
        return this.applicationContext.getResources(this.path);
    }
}
