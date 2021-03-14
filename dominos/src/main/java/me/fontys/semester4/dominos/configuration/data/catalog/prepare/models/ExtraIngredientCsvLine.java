package me.fontys.semester4.dominos.configuration.data.catalog.prepare.models;

import com.opencsv.bean.CsvBindByPosition;

import java.math.BigDecimal;

public class ExtraIngredientCsvLine {

    @CsvBindByPosition(position = 0)
    private final String ingredientName;
    @CsvBindByPosition(position = 1)
    private final BigDecimal addPrice;

    public ExtraIngredientCsvLine(String ingredientName, BigDecimal addPrice) {
        this.ingredientName = ingredientName;
        this.addPrice = addPrice;
    }

    public BigDecimal getAddPrice() {
        return addPrice;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    @Override
    public String toString() {
        return "ExtraIngredientSurchargeRecord{" +
                "ingredient='" + ingredientName + '\'' +
                ", addPrice='" + addPrice + '\'' +
                '}';
    }
}