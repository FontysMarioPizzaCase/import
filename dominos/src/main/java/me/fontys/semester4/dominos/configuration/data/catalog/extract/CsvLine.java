package me.fontys.semester4.dominos.configuration.data.catalog.extract;

import java.math.BigDecimal;

public class CsvLine {
    private String categoryName;
    private String subCategoryName;
    private String productName;
    private String productDescription;
    private BigDecimal price;
    private BigDecimal deliveryFee;
    private boolean isSpicy;
    private boolean isVegetarian;
    private boolean isAvailable;
    private int ingredientPortion;
    private String ingredientName;
    private String standardPizzasauce;


    public CsvLine(String categoryName, String subCategoryName, String productName,
                   String productDescription, BigDecimal price, BigDecimal deliveryFee,
                   boolean isSpicy, boolean isVegetarian, boolean isAvailable,
                   int ingredientPortion, String ingredientName, String standardPizzasauce) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.isSpicy = isSpicy;
        this.isVegetarian = isVegetarian;
        this.isAvailable = isAvailable;
        this.ingredientPortion = ingredientPortion;
        this.ingredientName = ingredientName;
        this.standardPizzasauce = standardPizzasauce;
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

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public boolean isSpicy() {
        return isSpicy;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getIngredientPortion() {
        return ingredientPortion;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getStandardPizzasauce() {
        return standardPizzasauce;
    }
}
