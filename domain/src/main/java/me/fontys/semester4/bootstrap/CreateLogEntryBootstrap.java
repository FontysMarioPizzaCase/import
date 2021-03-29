package me.fontys.semester4.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class CreateLogEntryBootstrap {

    @Value("classpath:createLogEntry.sql")
    private Resource createLogProcResource;

    private final ApplicationContext applicationContext;

    @Autowired
    public CreateLogEntryBootstrap(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean(name = "createLogEntry")
    public Resource getResource() throws IOException {
        return this.createLogProcResource;
    }
}
