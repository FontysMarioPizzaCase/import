package me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models;

import java.math.BigDecimal;

public class PizzaIngredientsCsvLine {
    private final String categoryName;
    private final String subCategoryName;
    private final String productName;
    private final String productDescription;
    private final BigDecimal price;
    private final BigDecimal deliveryFee;
    private final boolean isSpicy;
    private final boolean isVegetarian;
    private final boolean isAvailable;
    private final int ingredientPortion;
    private final String ingredientName;
    private final String standardPizzasauce;


    public PizzaIngredientsCsvLine(String categoryName, String subCategoryName, String productName,
                                   String productDescription, BigDecimal price, boolean isSpicy, boolean isVegetarian, boolean isAvailable, BigDecimal deliveryFee,
                                   String ingredientName, int ingredientPortion, String standardPizzasauce) {
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

    public PizzaIngredientsCsvLine(ProductCsvLine productData, BigDecimal deliveryFee, boolean isAvailable,
                                   int ingredientPortion, String ingredientName, String standardPizzasauce) {
        this(productData.getCategoryName(), productData.getSubCategoryName(), productData.getProductName(),
                productData.getProductDescription(), productData.getPrice(), productData.isSpicy(),
                productData.isVegetarian(), isAvailable, deliveryFee, ingredientName, ingredientPortion,
                standardPizzasauce);
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
