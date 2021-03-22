package me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models;

public interface HasProductData {
    String getCategoryName();
    String getSubCategoryName();
    String getProductName();
    String getProductDescription();
    String getPrice();
    String getIsSpicy();
    String getIsVegetarian();
}
