package me.fontys.semester4.dominos.configuration.data.order.formatter;

import java.lang.reflect.Array;
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
        this.addDateFormats(dutch);
        this.addDateFormats(english);
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

    public void addDateFormats(Locale locale) {
        this.dateFormats.add(new SimpleDateFormat("EEEE dd MMM yyyy hh:mm a", locale));
        this.dateFormats.add(new SimpleDateFormat("EEEE dd MMM yyyy hh:mm", locale));
        this.dateFormats.add(new SimpleDateFormat("EEEE dd MMM yyyy", locale));
        this.dateFormats.add(new SimpleDateFormat("EEEE, dd, MMM, yyyy hh:mm a", locale));
        this.dateFormats.add(new SimpleDateFormat("EEEE, dd, MMM, yyyy hh:mm", locale));
        this.dateFormats.add(new SimpleDateFormat("EEEE, dd, MMM, yyyy", locale));
        this.dateFormats.add(new SimpleDateFormat("EEEE, MMM dd, yyyy hh:mm a", locale));
        this.dateFormats.add(new SimpleDateFormat("EEEE, MMM dd, yyyy hh:mm", locale));
        this.dateFormats.add(new SimpleDateFormat("EEEE, MMM dd, yyyy", locale));
    }
}
