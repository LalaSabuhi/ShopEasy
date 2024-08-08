package az.informix.ShopEasy.service;

import az.informix.ShopEasy.model.Product;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    public Product saveProduct(Product product);
}
