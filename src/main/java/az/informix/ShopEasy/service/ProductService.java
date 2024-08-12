package az.informix.ShopEasy.service;

import az.informix.ShopEasy.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    public Product saveProduct(Product product);
    public List<Product> getAllProducts();
    public List<Product> getAllActiveProducts(String product);
    public Boolean deleteProduct(Integer id);
    public Product findProductById(Integer id);

}
