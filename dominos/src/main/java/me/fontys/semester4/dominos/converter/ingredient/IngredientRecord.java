package me.fontys.semester4.dominos.converter.ingredient;

import com.opencsv.bean.CsvBindByPosition;

public class IngredientRecord {

    @CsvBindByPosition(position = 0)
    private String categorie;
    @CsvBindByPosition(position = 1)
    private String subcategorie;
    @CsvBindByPosition(position = 2)
    private String productnaam;
    @CsvBindByPosition(position = 3)
    private String productomschrijving;
    @CsvBindByPosition(position = 4)
    private String prijs;
    @CsvBindByPosition(position = 5)
    private String bezorgtoeslag;
    @CsvBindByPosition(position = 6)
    private String spicy;
    @CsvBindByPosition(position = 7)
    private String vegetarisch;
    @CsvBindByPosition(position = 8)
    private String beschikbaar;
    @CsvBindByPosition(position = 9)
    private String aantalkeer_ingredient;
    @CsvBindByPosition(position = 10)
    private String ingredientnaam;
    @CsvBindByPosition(position = 11)
    private String pizzasaus_standaard;

    public IngredientRecord(){}

    public IngredientRecord(String categorie,
                            String subcategorie,
                            String productnaam,
                            String productomschrijving,
                            String prijs,
                            String bezorgtoeslag,
                            String spicy,
                            String vegetarisch,
                            String beschikbaar,
                            String aantalkeer_ingredient,
                            String ingredientnaam,
                            String pizzasaus_standaard) {
        this.categorie = categorie;
        this.subcategorie = subcategorie;
        this.productnaam = productnaam;
        this.productomschrijving = productomschrijving;
        this.prijs = prijs;
        this.bezorgtoeslag = bezorgtoeslag;
        this.spicy = spicy;
        this.vegetarisch = vegetarisch;
        this.beschikbaar = beschikbaar;
        this.aantalkeer_ingredient = aantalkeer_ingredient;
        this.ingredientnaam = ingredientnaam;
        this.pizzasaus_standaard = pizzasaus_standaard;
    }

    @Override
    public String toString() {
        return "InputLine{" +
                "categorie='" + categorie + '\'' +
                ", subcategorie='" + subcategorie + '\'' +
                ", productnaam='" + productnaam + '\'' +
                ", productomschrijving='" + productomschrijving + '\'' +
                ", prijs='" + prijs + '\'' +
                ", bezorgtoeslag='" + bezorgtoeslag + '\'' +
                ", spicy='" + spicy + '\'' +
                ", vegetarisch='" + vegetarisch + '\'' +
                ", beschikbaar='" + beschikbaar + '\'' +
                ", aantalkeer_ingredient='" + aantalkeer_ingredient + '\'' +
                ", ingredientnaam='" + ingredientnaam + '\'' +
                ", pizzasaus_standaard='" + pizzasaus_standaard + '\'' +
                '}';
    }
}