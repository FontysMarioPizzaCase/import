package me.fontys.semester4.dominos.configuration.data.catalog.util;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CleanerUtil {
    public String cleanString(String string) {
        return string.trim().toLowerCase()
                // removes all bytes that are not readable ASCII chars
                .replaceAll("[^\\x20-\\x7E]", ""); // TODO: loggen!
    }

    public BigDecimal cleanPrice(String priceString) {
        String temp = priceString.trim()
                .replace(',', '.')
                .replaceAll("[^0-9.]", "");
        return new BigDecimal(temp);
    }

    public int cleanInteger(String string) {
        return Integer.parseInt(cleanString(string));
    }

    public boolean cleanBool(String string, String trueValue) {
        return string.trim().toLowerCase()
                .replaceAll("[^\\x20-\\x7E]", "")
                .equalsIgnoreCase(trueValue);
    }
}

