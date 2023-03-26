package controllers;

import exceptions.InvalidEanException;
import exceptions.InvalidTargetEanException;
import exceptions.ProductAlreadyExistsException;
import models.Product;
import models.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import services.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static util.Constants.*;

@RestController
@CrossOrigin(origins = {"*"})
public class ProductsController {

    @Autowired ProductService productService;

    private static final Map<String, String> TARGET_MAP = Map.of(
            "1", TARGET_MERCADONA_SPAIN,
            "2", TARGET_MERCADONA_SPAIN,
            "3", TARGET_MERCADONA_SPAIN,
            "4", TARGET_MERCADONA_SPAIN,
            "5", TARGET_MERCADONA_SPAIN,
            "6", TARGET_MERCADONA_PORTUGAL,
            "8", TARGET_WAREHOUSE,
            "9", TARGET_OFFICE,
            "0", TARGET_HIVE
    );

    /**
     * GET endpoint for checking if the product microservice is running.
     * @return A ResponseEntity with HTTP status code 200 and the message "Microservice products is running".
     * If an exception occurs, a ResponseEntity with HTTP status code 500 and the exception message is returned.
     */
    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> ping() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body("Microservice products is running");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("" + ex.getMessage());
        }
    }

    /**
     * GET endpoint for getting details of a product by its EAN code.
     * @param ean The EAN code of the product.
     * @return A ResponseEntity with HTTP status code 200 and the product details in JSON format.
     * If the EAN code is invalid or the product details are not found, a ResponseEntity with HTTP status code 500 and the exception message is returned.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USERS')")
    @GetMapping(value = "/productdetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> productDetail(@RequestParam("ean") String ean){
        try {
            ProductDto product = validateEan(ean);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }catch (InvalidEanException | InvalidTargetEanException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("" + ex.getMessage());
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("" + ex.getMessage());
        }
    }

    /**
     * GET endpoint for getting a list of all products.
     * @return A ResponseEntity with HTTP status code 200 and a list of all products in JSON format.
     * If an exception occurs, a ResponseEntity with HTTP status code 500 and the exception message is returned.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USERS')")
    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> products(){
        try{
            List<Product> productList = productService.getAllProducts();
            List<ProductDto> productResponseList = productList.stream()
                    .map(product -> new ProductDto(product.getProvider(), product.getProductCode(), product.getTarget()))
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(productResponseList);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("" + ex.getMessage());
        }
    }

    /**
     * POST endpoint for adding a new product.
     * @param ean The EAN code of the product to be added.
     * @return A ResponseEntity with HTTP status code 200 and the list of all products in JSON format, including the newly added product.
     * If the EAN code is invalid or the product details are not found, a ResponseEntity with HTTP status code 500 and the exception message is returned.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/addproduct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProduct(@RequestParam("ean") String ean){
        try {
            ProductDto productDto = validateEan(ean);
            List<Product> productList = productService.addProduct(productDto.getProvider(), productDto.getProductCode(),
                    productDto.getTarget());
            List<ProductDto> productResponseList = productList.stream()
                    .map(product -> new ProductDto(product.getProvider(), product.getProductCode(), product.getTarget()))
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(productResponseList);
        }catch (InvalidEanException | InvalidTargetEanException | ProductAlreadyExistsException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("" + ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("" + ex.getMessage());
        }
    }

    /**
     * Updates a product with the specified ID using the given EAN code.
     *
     * @param id the ID of the product to update
     * @param ean the EAN code to use for the update
     * @return a ResponseEntity with the updated product as a ProductDto in the body if the update was successful,
     *         otherwise a ResponseEntity with an error message in the body and an HTTP status of 500 (Internal Server Error)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/updateproduct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProduct(@RequestParam ("id") Long id, @RequestParam("ean") String ean){
        try {
            ProductDto productDto = validateEan(ean);
            Product product = productService.updateProduct(id, productDto.getProvider(), productDto.getProductCode(),
                    productDto.getTarget());
            return ResponseEntity.status(HttpStatus.OK).body(new ProductDto(product.getProvider(),
                    product.getProductCode(), product.getTarget()));
        }catch (InvalidEanException | InvalidTargetEanException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("" + ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("" + ex.getMessage());
        }
    }

    /**
     * Deletes a product with the specified EAN code.
     *
     * @param ean the EAN code of the product to delete
     * @return a ResponseEntity with a success message in the body,
     *         otherwise a ResponseEntity with an error message in the body and an HTTP status of 500 (Internal Server Error)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/deleteproduct")
    public ResponseEntity<String> deleteProduct(@RequestParam("ean") String ean){
        try {
            ProductDto productDto = validateEan(ean);
            String message = productService.deleteProduct(productDto.getProvider(), productDto.getProductCode(),
                    productDto.getTarget());
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }catch (InvalidEanException | InvalidTargetEanException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("" + ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("" + ex.getMessage());
        }
    }

    /**
     * Validates an EAN code and returns a ProductDto containing the provider, product code, and target.
     *
     * @param ean the EAN code to validate
     * @return a ProductDto containing the provider, product code, and target if the validation was successful
     * @throws Exception if the EAN code is invalid (e.g. incorrect length or contains non-numeric characters)
     */
    private ProductDto validateEan(String ean) throws InvalidEanException, InvalidTargetEanException {
        String regex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ean);
        if (ean.length() != 13){
            throw new InvalidEanException("the ean has an incorrect length");
        }else if (!matcher.matches()){
            throw new InvalidEanException("the ean can only have numbers");
        }else {
            String provider = getProvider(ean.substring(0, 7));
            String productCodeEan = ean.substring(7, 12);
            String target = getTarget(ean.substring(12));

            return new ProductDto(provider, productCodeEan, target);
        }
    }

    /**
     * Returns the target associated with the specified target EAN code.
     *
     * @param targetEan the target EAN code to lookup
     * @return the target associated with the target EAN code
     * @throws Exception if the target EAN code is invalid (i.e. not found in the TARGET_MAP)
     */
    private String getTarget(String targetEan) throws InvalidTargetEanException {
        String target = TARGET_MAP.get(targetEan);
        if (target == null) {
            throw new InvalidTargetEanException("The target is invalid");
        }
        return target;
    }

    /**
     * Returns the provider associated with the specified provider EAN code.
     *
     * @param providerEan the provider EAN code to lookup
     * @return the provider associated with the provider EAN code
     */
    private String getProvider(String providerEan){

        if (Objects.equals(providerEan, NUMBER_HACENDADO)){
            return PROVIDER_MERCADONA;
        }else{
            return PROVIDER_OTHER;
        }
    }

}
