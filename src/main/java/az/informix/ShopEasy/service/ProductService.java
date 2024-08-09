package az.informix.ShopEasy.service;

import az.informix.ShopEasy.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    public Product saveProduct(Product product);
    public List<Product> getAllProducts();
    public Boolean deleteProduct(Integer id);
}
