package me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models;

import java.math.BigDecimal;

public class CrustCsvLine {

    private final String crustName;
    private final String description;
    private final int size;
    private final BigDecimal addPrice;
    private final boolean isAvailable;

    public CrustCsvLine(String crustName, String description, int size, BigDecimal addPrice, boolean isAvailable) {
        this.crustName = crustName;
        this.description = description;
        this.size = size;
        this.addPrice = addPrice;
        this.isAvailable = isAvailable;
    }

    public BigDecimal getAddPrice() {
        return addPrice;
    }

    public String getCrustName() {
        return crustName;
    }

    public String getDescription() {
        return description;
    }

    public int getSize() {
        return size;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public String toString() {
        return "CrustCsvLine{" +
                "ingredientName='" + crustName + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                ", addPrice=" + addPrice +
                ", isAvailable=" + isAvailable +
                '}';
    }
}