package dao;

import models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

    /**
     * Finds all products.
     *
     * @return a list of all products.
     */
    List<Product> findAll();

    /**
     * Saves a new product.
     *
     * @param newProduct the new product to be saved.
     */
    void save(Product newProduct);

    /**
     * Finds a product by its ID.
     *
     * @param id the ID of the product to be found.
     * @return the found product, or null if not found.
     */
    Product findById(Long id);

    /**
     * Deletes a product.
     *
     * @param product the product to be deleted.
     */
    void deleteProduct(Product product);

    /**
     * Finds a product by its EAN details (provider, product code, and target).
     *
     * @param provider the provider of the product.
     * @param productCode the product code of the product.
     * @param target the target of the product.
     * @return the found product, or object empty.
     */
    Optional<Product> findProductByEanDetails(String provider, String productCode, String target);
}
