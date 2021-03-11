package me.fontys.semester4.dominos.configuration.data.order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDateFormatter {

    private final List<DateFormat> dateFormats;

    public OrderDateFormatter() {
        this.dateFormats = new ArrayList<>();

        Locale dutch = Locale.forLanguageTag("nl-nl");
        Locale english = Locale.forLanguageTag("en-en");
        this.dateFormats.add(new SimpleDateFormat("EEEE dd MMM yyyy", dutch));
        this.dateFormats.add(new SimpleDateFormat("EEEE, dd, MMM, yyyy", dutch));
        this.dateFormats.add(new SimpleDateFormat("EEEE, MMM dd, yyyy", dutch));
        this.dateFormats.add(new SimpleDateFormat("EEEE dd MMM yyyy", english));
        this.dateFormats.add(new SimpleDateFormat("EEEE, dd, MMM, yyyy", english));
        this.dateFormats.add(new SimpleDateFormat("EEEE, MMM dd, yyyy", english));
    }

    public Date fromString(String date) {
        if (date == null || date.isEmpty()) return null;

        for (DateFormat format : this.dateFormats) {
            try {
                return format.parse(date);
            } catch (Exception ignored) {}
        }
        return null;
    }
}
