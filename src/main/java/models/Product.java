package models;

import javax.persistence.*;

/**
 This class represents a product entity and is mapped to the "products" table in the database.
 */
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String provider;
    private String productCode;
    private String target;

    /**
     Default constructor.
     */
    public Product() {
    }

    /**
     Returns the ID of this product.
     @return the ID of this product.
     */
    public Long getId() {
        return id;
    }

    /**
     Sets the ID of this product.
     @param id the ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     Returns the provider of this product.
     @return the provider of this product.
     */
    @Column(name = "provider_name")
    public String getProvider() {
        return provider;
    }

    /**
     Sets the provider of this product.
     @param provider the provider to set.
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     Returns the product code of this product.
     @return the product code of this product.
     */
    @Column(name = "product_code")
    public String getProductCode() {
        return productCode;
    }

    /**
     Sets product code of this product.
     @param productCode the product to set.
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     Returns the target of this product.
     @return the target of this product.
     */
    @Column(name = "target")
    public String getTarget() {
        return target;
    }

    /**
     Sets target of this product.
     @param target the product to set.
     */
    public void setTarget(String target) {
        this.target = target;
    }
}
