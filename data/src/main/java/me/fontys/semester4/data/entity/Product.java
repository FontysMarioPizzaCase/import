package me.fontys.semester4.data.entity;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid", nullable = false)
    private Long productid;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "spicy")
    private Boolean spicy;

    @Column(name = "vegetarian")
    private Boolean vegetarian;

    @Column(name = "deliveryfee")
    private BigDecimal deliveryfee;

    @Column(name = "taxrate")
    private Double taxrate;

    @Column(name = "imagepath")
    private String imagepath;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable( name = "category_product",
            joinColumns = @JoinColumn(name="productid"),
            inverseJoinColumns = @JoinColumn(name="catid"))
    private Set<Category> categories;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable( name = "ingredient_product",
            joinColumns = @JoinColumn(name="productid"),
            inverseJoinColumns = @JoinColumn(name="ingredientid"))
    private Set<Ingredient> ingredients;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.MERGE})
    private List<ProductPrice> prices;

    public Product(Long productid, String name, String description, Boolean spicy,
                   Boolean vegetarian, BigDecimal deliveryfee, Double taxrate,
                   String imagepath) {
        this.productid = productid;
        this.name = name;
        this.description = description;
        this.spicy = spicy;
        this.vegetarian = vegetarian;
        this.deliveryfee = deliveryfee;
        this.taxrate = taxrate;
        this.imagepath = imagepath;
        categories = new HashSet<>();
        ingredients = new HashSet<>();
    }

    protected Product() {
        this(null, null, null, null,
                null, null, null, null);
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getSpicy() {
        return spicy;
    }

    public void setSpicy(boolean spicy) {
        this.spicy = spicy;
    }

    public boolean getVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public BigDecimal getDeliveryfee() {
        return deliveryfee;
    }

    public void setDeliveryfee(BigDecimal deliveryfee) {
        this.deliveryfee = deliveryfee;
    }

    public Double getTaxrate() {
        return taxrate;
    }

    public void setTaxrate(Double taxrate) {
        this.taxrate = taxrate;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    @Transactional // TODO: is this wanted in every tostring?
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product{");
        sb.append("productid=").append(productid);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", spicy='").append(spicy).append('\'');
        sb.append(", vegetarian='").append(vegetarian).append('\'');
        sb.append(", deliveryfee='").append(deliveryfee).append('\'');
        sb.append(", taxrate=").append(taxrate);
        sb.append(", imagepath='").append(imagepath).append('\'');
        sb.append(", categoryIds={");
        int i = 0;
        for (Category category : categories) {
            sb.append(category.getCatid());
            if (i < categories.size() - 1) sb.append(", ");
            i++;
        }
        sb.append("}, ingredientIds={");
        i = 0;
        for (Ingredient ingredient : ingredients) {
            sb.append(ingredient.getIngredientid());
            if (i < categories.size() - 1) sb.append(", ");
            i++;
        }
        sb.append("}}");

        return sb.toString();
    }
}
