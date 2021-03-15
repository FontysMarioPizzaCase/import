package me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts.csv_models;

import com.opencsv.bean.CsvBindByPosition;

public class CrustRawCsvLine {

    @CsvBindByPosition(position = 0)
    private String crustName;
    @CsvBindByPosition(position = 1)
    private String size;
    @CsvBindByPosition(position = 2)
    private String description;
    @CsvBindByPosition(position = 3)
    private String addPrice;
    @CsvBindByPosition(position = 4)
    private String isAvailable;

    public CrustRawCsvLine() {
    } // keep public for CsvToBeanBuilder

    public String getAddPrice() {
        return addPrice;
    }

    public String getCrustName() {
        return crustName;
    }

    public String getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    @Override
    public String toString() {
        return "CrustRawCsvLine{" +
                "crustName='" + crustName + '\'' +
                ", size='" + size + '\'' +
                ", description='" + description + '\'' +
                ", addPrice='" + addPrice + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                '}';
    }
}