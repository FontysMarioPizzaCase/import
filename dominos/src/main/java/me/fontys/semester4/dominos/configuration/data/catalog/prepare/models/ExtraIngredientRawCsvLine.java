package me.fontys.semester4.dominos.configuration.data.catalog.prepare.models;

import com.opencsv.bean.CsvBindByPosition;

public class ExtraIngredientRawCsvLine {

    @CsvBindByPosition(position = 0)
    private String ingredientName;
    @CsvBindByPosition(position = 1)
    private String addPrice;

    public ExtraIngredientRawCsvLine() {
    } // keep public for CsvToBeanBuilder

    public ExtraIngredientRawCsvLine(String ingredientName, String addPrice) {
        this.ingredientName = ingredientName;
        this.addPrice = addPrice;
    }

    public String getAddPrice() {
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