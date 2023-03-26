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

@Repository
public class ProductDaoImpl implements ProductDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired ProductJpaSpring productJpaSpring;

    @Override
    public List<Product> findAll() {
        return productJpaSpring.findAll();
    }

    @Override
    public void save(Product newProduct) {
        productJpaSpring.save(newProduct);
    }

    @Override
    public Product findById(Long id) {
        return productJpaSpring.findById(id).orElse(null);
    }

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

    @Override
    public void deleteProduct(Product product) {
        productJpaSpring.delete(product);
    }
}
