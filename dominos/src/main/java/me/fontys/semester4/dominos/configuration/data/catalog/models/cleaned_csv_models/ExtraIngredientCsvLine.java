package me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models;

import java.math.BigDecimal;

public class ExtraIngredientCsvLine {

    private final String ingredientName;
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