package me.fontys.semester4.bootstrap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class CreateLogEntryBootstrap {

    private final Resource createLogEntry;
    public CreateLogEntryBootstrap(@Value("classpath:createLogEntry.sql") Resource createLogEntry) {
        this.createLogEntry = createLogEntry;
    }

    @Bean(name = "createLogEntryBean")
    public Resource getResources() throws IOException {
        return this.createLogEntry;
    }
}
