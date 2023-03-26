package dao;

import models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

    List<Product> findAll();

    void save(Product newProduct);

    Product findById(Long id);

    void deleteProduct(Product product);

    Optional<Product> findProductByEanDetails(String provider, String productCode, String target);
}
