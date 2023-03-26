package services.impl;

import dao.ProductDao;
import models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import services.ProductService;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired ProductDao productDao;

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @Override
    @Transactional
    public List<Product> addProduct(String provider, String productCode, String target) throws Exception {
        Optional<Product> product = productDao.findProductByEanDetails(provider, productCode, target);
        if (product.isEmpty()){
            Product newProduct = new Product();
            newProduct.setProvider(provider);
            newProduct.setProductCode(productCode);
            newProduct.setTarget(target);
            productDao.save(newProduct);
            return productDao.findAll();
        }else{
            throw new Exception("The product already exist");
        }
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, String provider, String productCode, String target) {

        Product product = productDao.findById(id);
        if (product != null){
            product.setProvider(provider);
            product.setProductCode(productCode);
            product.setTarget(target);
            productDao.save(product);
        }
        return product;
    }

    @Override
    @Transactional
    public String deleteProduct(String provider, String productCode, String target) {

        Optional<Product> product = productDao.findProductByEanDetails(provider, productCode, target);
        if (product.isPresent()){
            productDao.deleteProduct(product.get());
            return "Delete product";
        }else{
            return "Product not exist";
        }

    }
}
