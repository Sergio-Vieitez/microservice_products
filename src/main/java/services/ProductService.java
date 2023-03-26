package services;

import models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<Product> getAllProducts();

    List<Product> addProduct(String provider, String productCode, String target) throws Exception;

    Product updateProduct(Long id, String provider, String productCode, String target);

    String deleteProduct(String provider, String productCode, String target);
}
