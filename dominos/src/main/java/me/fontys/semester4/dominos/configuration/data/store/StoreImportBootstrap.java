package me.fontys.semester4.dominos.configuration.data.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class StoreImportBootstrap {

    @Value("${import.store.wildcard}")
    private final String path = "./import-data/winkels*.txt";
    private final ApplicationContext applicationContext;

    @Autowired
    public StoreImportBootstrap(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean(name = "stores")
    public Resource[] getResources() throws IOException {
        return this.applicationContext.getResources(this.path);
    }
}
