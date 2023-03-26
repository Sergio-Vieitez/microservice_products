package dao.impl;

import dao.ProductDao;
import dao.ProductJpaSpring;
import models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 This class provides an implementation of the {@link ProductDao} interface
 using JPA with Spring. It allows CRUD (create, read, update, delete)
 operations on Product entities.
 */
@Repository
public class ProductDaoImpl implements ProductDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired ProductJpaSpring productJpaSpring;

    /**
     Retrieves all products from the database.
     @return a list of all products
     */
    @Override
    public List<Product> findAll() {
        return productJpaSpring.findAll();
    }

    /**
     Saves a new product in the database.
     @param newProduct the product to be saved
     */
    @Override
    public void save(Product newProduct) {
        productJpaSpring.save(newProduct);
    }

    /**
     Finds a product by its ID.
     @param id the ID of the product to be found
     @return the found product, or null if no product with the given ID exists
     */
    @Override
    public Product findById(Long id) {
        return productJpaSpring.findById(id).orElse(null);
    }

    /**
     Finds a product by its EAN details, including provider,
     product code, and target.
     @param provider the provider of the product
     @param productCode the product code of the product
     @param target the target of the product
     @return an {@link Optional} of the found product, or an empty optional if no
     product with the given EAN details exists
     */
    @Override
    public Optional<Product> findProductByEanDetails(String provider, String productCode, String target) {
        String queryString = "SELECT p " +
                "FROM Product p " +
                "WHERE p.provider = :provider " +
                "AND p.productCode = :productCode " +
                "AND p.target = :target";
        return entityManager.createQuery(queryString)
                .setParameter("provider", provider)
                .setParameter("productCode", productCode)
                .setParameter("target", target)
                .getResultList().stream().findFirst();

    }

    /**
     Deletes a product from the database.
     @param product the product to be deleted
     */
    @Override
    public void deleteProduct(Product product) {
        productJpaSpring.delete(product);
    }
}
