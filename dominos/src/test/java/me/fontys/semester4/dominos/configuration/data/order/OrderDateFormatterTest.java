package me.fontys.semester4.dominos.configuration.data.order;

import me.fontys.semester4.dominos.configuration.data.order.formatter.OrderDateFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Order date formatter")
public class OrderDateFormatterTest {

    private OrderDateFormatter formatter;

    @BeforeEach
    public void setup() {
        this.formatter = new OrderDateFormatter();
    }

    @Test
    @DisplayName("Test Dutch weekday-day-month-year (EEEE dd MMM yyyy)")
    public void testDutchWeekdayDayMonthYearNoComma() {
        Date act = this.formatter.fromString("maandag 3 september 2018");
        assertDate(act);
    }

    @Test
    @DisplayName("Test Dutch weekday-day-month-year (EEEE, MMM dd, yyyy)")
    public void testDutchWeekdayMonthDayYearComma() {
        Date act = this.formatter.fromString("maandag, september 3, 2018");
        assertDate(act);
    }

    @Test
    @DisplayName("Test Dutch weekday-day-month-year (EEEE, dd, MMM, yyyy)")
    public void testDutchWeekdayDayMonthYearComma() {
        Date act = this.formatter.fromString("maandag, 3, september, 2018");
        assertDate(act);
    }

    @Test
    @DisplayName("Test English weekday-day-month-year (EEEE dd MMM yyyy)")
    public void testEnglishWeekdayDayMonthYearNoComma() {
        Date act = this.formatter.fromString("Monday 3 September 2018");
        assertDate(act);
    }

    @Test
    @DisplayName("Test English weekday-day-month-year (EEEE, MMM dd, yyyy)")
    public void testEnglishWeekdayMonthDayYearComma() {
        Date act = this.formatter.fromString("Monday, September 3, 2018");
        assertDate(act);
    }

    @Test
    @DisplayName("Test English weekday-day-month-year (EEEE, dd, MMM, yyyy)")
    public void testEnglishWeekdayDayMonthYearComma() {
        Date act = this.formatter.fromString("Monday, 3, September, 2018");
        assertDate(act);
    }

    private void assertDate(Date act) {

        assertNotNull(act);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(act);

        assertEquals(3, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(8, calendar.get(Calendar.MONTH));
        assertEquals(2018, calendar.get(Calendar.YEAR));
    }
}
