package dao;

import models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 This interface extends {@link JpaRepository} and provides CRUD (create, read,
 update, delete) operations on {@link Product} entities using Spring Data JPA.
 */
public interface ProductJpaSpring extends JpaRepository<Product, Long> {

}
