package me.fontys.semester4.dominos.configuration.data.catalog.prepare.models;

import com.opencsv.bean.CsvBindByPosition;

public class OverigProductRawCsvLine {

    @CsvBindByPosition(position = 0)
    private String categoryName;
    @CsvBindByPosition(position = 1)
    private String subCategoryName;
    @CsvBindByPosition(position = 2)
    private String productName;
    @CsvBindByPosition(position = 3)
    private String productDescription;
    @CsvBindByPosition(position = 4)
    private String price;
    @CsvBindByPosition(position = 5)
    private String isSpicy;
    @CsvBindByPosition(position = 6)
    private String isVegetarian;

    public OverigProductRawCsvLine() {
    } // keep public for CsvToBeanBuilder

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

    public String getPrice() {
        return price;
    }

    public String getIsSpicy() {
        return isSpicy;
    }

    public String getIsVegetarian() {
        return isVegetarian;
    }

    @Override
    public String toString() {
        return "OverigProductRawCsvLine{" +
                "category='" + categoryName + '\'' +
                ", subCategory='" + subCategoryName + '\'' +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", price='" + price + '\'' +
                ", isSpicy='" + isSpicy + '\'' +
                ", isVegetarian='" + isVegetarian + '\'' +
                '}';
    }
}