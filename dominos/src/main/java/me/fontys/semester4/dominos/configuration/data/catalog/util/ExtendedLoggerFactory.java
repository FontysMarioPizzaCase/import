package me.fontys.semester4.dominos.configuration.data.catalog.util;


import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ExtendedLoggerFactory {
    public ExtendedLogger get(Logger logger){
        return new ExtendedLogger(logger);
    }
}
