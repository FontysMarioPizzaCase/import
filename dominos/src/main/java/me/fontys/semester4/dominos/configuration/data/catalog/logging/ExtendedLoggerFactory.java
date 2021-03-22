package me.fontys.semester4.dominos.configuration.data.catalog.logging;


import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ExtendedLoggerFactory {
    public ExtendedLogger extendedLogger(Logger logger){
        return new ExtendedLogger(logger);
    }
}
