package me.fontys.semester4.dominos.configuration.data.catalog.pizzawithingredients.extract;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CleanerUtil {
    public BigDecimal cleanPrice(String priceString) {
        String temp = priceString.trim()
                .replace(',', '.')
                .replaceAll("[^0-9.]", "");
        return new BigDecimal(temp);
    }

    public String cleanString(String string) {
        return string.trim().toUpperCase();
    }
}

