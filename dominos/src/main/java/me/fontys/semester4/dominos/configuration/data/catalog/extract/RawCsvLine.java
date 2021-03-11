package me.fontys.semester4.dominos.configuration.data.catalog.extract;

import com.opencsv.bean.CsvBindByPosition;

public class RawCsvLine {

    @CsvBindByPosition(position = 0)
    private String category;
    @CsvBindByPosition(position = 1)
    private String subCategory;
    @CsvBindByPosition(position = 2)
    private String productName;
    @CsvBindByPosition(position = 3)
    private String productDescription;
    @CsvBindByPosition(position = 4)
    private String price;
    @CsvBindByPosition(position = 5)
    private String deliveryFee;
    @CsvBindByPosition(position = 6)
    private String isSpicy;
    @CsvBindByPosition(position = 7)
    private String isVegetarian;
    @CsvBindByPosition(position = 8)
    private String isAvailable;
    @CsvBindByPosition(position = 9)
    private String ingredientPortion;
    @CsvBindByPosition(position = 10)
    private String ingredientName;
    @CsvBindByPosition(position = 11)
    private String standardPizzasauce;

    public RawCsvLine() {
    } // keep public for CsvToBeanBuilder

    public RawCsvLine(String category, String subCategory, String productName,
                      String productDescription, String price, String deliveryFee,
                      String isSpicy, String isVegetarian, String isAvailable,
                      String ingredientPortion, String ingredientName,
                      String standardPizzasauce) {
        this.category = category;
        this.subCategory = subCategory;
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
        return category;
    }

    public String getSubCategoryName() {
        return subCategory;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getPrice() {
        return price;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public String getIsSpicy() {
        return isSpicy;
    }

    public String getIsVegetarian() {
        return isVegetarian;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public String getIngredientPortion() {
        return ingredientPortion;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getStandardPizzasauce() {
        return standardPizzasauce;
    }

    @Override
    public String toString() {
        return "PizzaAndIngredientRecord{" +
                "category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", price='" + price + '\'' +
                ", deliveryFee='" + deliveryFee + '\'' +
                ", isSpicy='" + isSpicy + '\'' +
                ", isVegetarian='" + isVegetarian + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", ingredientPortion='" + ingredientPortion + '\'' +
                ", ingredientName='" + ingredientName + '\'' +
                ", standardPizzasauce='" + standardPizzasauce + '\'' +
                '}';
    }
}