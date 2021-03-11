//package me.fontys.semester4.dominos.configuration.data.extraingredients;
//
//import com.opencsv.bean.CsvBindByPosition;
//
//public class ExtraIngredientSurchargeRecord {
//
//    @CsvBindByPosition(position = 0)
//    private String ingredientName;
//    @CsvBindByPosition(position = 1)
//    private String addPrice;
//
//    public ExtraIngredientSurchargeRecord() {
//    } // keep public for CsvToBeanBuilder
//
//    public ExtraIngredientSurchargeRecord(String ingredientName, String addPrice) {
//        this.ingredientName = ingredientName;
//        this.addPrice = addPrice;
//    }
//
//    public String getAddPrice() {
//        return addPrice;
//    }
//
//    public String getIngredientName() {
//        return ingredientName;
//    }
//
//    @Override
//    public String toString() {
//        return "ExtraIngredientSurchargeRecord{" +
//                "ingredient='" + ingredientName + '\'' +
//                ", addPrice='" + addPrice + '\'' +
//                '}';
//    }
//}