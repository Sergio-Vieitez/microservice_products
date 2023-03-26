package dao;

import models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaSpring extends JpaRepository<Product, Long> {

}
