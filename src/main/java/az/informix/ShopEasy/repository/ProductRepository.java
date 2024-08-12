package az.informix.ShopEasy.repository;

import az.informix.ShopEasy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    public List<Product> findByCategory(String category);
    public List<Product> findByIsActiveTrue();
}
