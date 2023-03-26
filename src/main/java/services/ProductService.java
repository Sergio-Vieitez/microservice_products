package services;

import models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for managing products.
 */
@Service
public interface ProductService {

    /**
     * Retrieves a list of all products.
     * @return List of all products.
     */
    List<Product> getAllProducts();

    /**
     * Adds a new product with the given provider, product code, and target.
     * @param provider The provider of the product.
     * @param productCode The product code.
     * @param target The target of the product.
     * @return List of all products after adding the new product.
     * @throws Exception If there is an error adding the product.
     */
    List<Product> addProduct(String provider, String productCode, String target) throws Exception;

    /**
     * Updates the product with the given ID, setting the provider, product code, and target to the given values.
     * @param id The ID of the product to update.
     * @param provider The new provider of the product.
     * @param productCode The new product code.
     * @param target The new target of the product.
     * @return The updated product.
     */
    Product updateProduct(Long id, String provider, String productCode, String target);

    /**
     * Deletes the product with the given provider, product code, and target.
     * @param provider The provider of the product to delete.
     * @param productCode The product code of the product to delete.
     * @param target The target of the product to delete.
     * @return A message indicating whether the product was deleted successfully.
     */
    String deleteProduct(String provider, String productCode, String target);
}
