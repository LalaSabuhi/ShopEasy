package az.informix.ShopEasy.service;

import az.informix.ShopEasy.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    public Category saveCategory(Category category);
    public List<Category> getAllCategory();
    public Boolean existCategory(String name);
}
