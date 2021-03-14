package me.fontys.semester4.dominos.configuration.data.catalog.overige_producten.csv_models;

import com.opencsv.bean.CsvBindByPosition;

import java.math.BigDecimal;

public class OverigProductCsvLine {

    private final String categoryName;
    private final String subCategoryName;
    private final String productName;
    private final String productDescription;
    private final BigDecimal price;
    private final boolean isSpicy;
    private final boolean isVegetarian;

    public OverigProductCsvLine(String categoryName, String subCategoryName, String productName,
                                String productDescription, BigDecimal price, boolean isSpicy,
                                boolean isVegetarian) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.isSpicy = isSpicy;
        this.isVegetarian = isVegetarian;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isSpicy() {
        return isSpicy;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }
}