package me.fontys.semester4.dominos.configuration.data.order.formatter;

public class OrderPhoneNumberFormatter {

    public int fromString(String phoneNumber) {
        try {
            phoneNumber = phoneNumber.replace("-", "");
            return Integer.parseInt(phoneNumber);
        } catch (Exception ex) {
            return 0;
        }
    }
}
