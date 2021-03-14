package me.fontys.semester4.dominos.configuration.data.catalog.overige_producten;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class OverigeProductenImportBootstrap {

    @Value("${import.overigeproducten}")
    private final String path = "./import-data/Overige_producten*.csv";
    private final ApplicationContext applicationContext;

    @Autowired
    public OverigeProductenImportBootstrap(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean(name = "overigeProducten")
    public Resource[] getResources() throws IOException {
        return this.applicationContext.getResources(this.path);
    }
}
