package me.fontys.semester4.dominos.configuration.data.catalog.util;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PriceCleaner {
    public BigDecimal clean(String priceString) {
        String temp = priceString.trim()
                .replace(',', '.')
                .replaceAll("[^0-9.]", "");
        return new BigDecimal(temp);
    }
}
