package models;

/**
 This class represents a product data transfer object (DTO) that contains information about a product, including the provider,
 product code, and target.
 */
public class ProductDto {

    private String provider;
    private String productCode;
    private String target;

    /**
     Default constructor.
     */
    public ProductDto() {
    }

    /**
     Constructor for ProductDto that initializes the provider, product code, and target.
     @param provider the provider of the product.
     @param productCode the product code of the product.
     @param target the target of the product.
     */
    public ProductDto(String provider, String productCode, String target) {
        this.provider = provider;
        this.productCode = productCode;
        this.target = target;
    }

    /**
     Gets the provider of the product.
     @return the provider of the product.
     */
    public String getProvider() {
        return provider;
    }

    /**
     Sets the provider of the product.
     @param provider the provider of the product.
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     Gets the product code of the product.
     @return the product code of the product.
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     Sets the product code of the product.
     @param productCode the product code of the product.
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     Gets the target of the product.
     @return the target of the product.
     */
    public String getTarget() {
        return target;
    }

    /**
     Sets the target of the product.
     @param target the target of the product.
     */
    public void setTarget(String target) {
        this.target = target;
    }
}
