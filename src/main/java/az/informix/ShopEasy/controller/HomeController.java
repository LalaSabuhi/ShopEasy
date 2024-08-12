package az.informix.ShopEasy.controller;

import az.informix.ShopEasy.model.Product;
import az.informix.ShopEasy.service.CategoryService;
import az.informix.ShopEasy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/register")
    public String register(){
        return "register";
    }
    @GetMapping("/products")
    public String products(Model model, @RequestParam(value="category", defaultValue = "") String category) {
        //System.out.println("category: " + category);
        model.addAttribute("products", productService.getAllActiveProducts(category));
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("paramValue", category);
        return "product";
    }
    @GetMapping("/product/{id}")
    public String product(@PathVariable int id, Model model){
        Product product = productService.findProductById(id);
        model.addAttribute("product", product);
        return "view_product";
    }

}
