// TODO: remove
//package me.fontys.semester4.data.repository;
//
//import me.fontys.semester4.data.entity.Ingredient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class IngredientRepositoryImpl {
//    @Autowired
//    IngredientRepository ingredientRepository;
//
//    public IngredientRepositoryImpl(){}
//
//    // TODO: remove?
////    @SuppressWarnings("unused")
////    public Ingredient saveOrUpdate(Ingredient newData) {
////        Optional<Ingredient> temp = this.ingredientRepository.findByNameIgnoreCase(newData.getName());
////
////        Ingredient ingredient;
////        if (temp.isPresent()) {
////            ingredient = temp.get();
////            updateProperties(ingredient, newData);
////        } else {
////            ingredient = newData;
////            this.ingredientRepository.save(ingredient);
////        }
////
////        return ingredient;
////    }
////
////    private void updateProperties(Ingredient ingredient, Ingredient newData) {
////        if(newData.getDescription() != null && !newData.getDescription().isEmpty()) ingredient.setDescription(newData.getDescription());
////        if(newData.getSize() != null) ingredient.setSize(newData.getSize());
////        if(newData.getAddprice() != null) ingredient.setAddprice(newData.getAddprice());
////        if(newData.isAvailable() != null) ingredient.setAvailable(newData.isAvailable());
////    }
//}
