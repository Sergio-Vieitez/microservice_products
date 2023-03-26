package models;

public class ProductDto {

    private String provider;
    private String productCode;
    private String target;

    public ProductDto() {
    }

    public ProductDto(String provider, String productCode, String target) {
        this.provider = provider;
        this.productCode = productCode;
        this.target = target;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
