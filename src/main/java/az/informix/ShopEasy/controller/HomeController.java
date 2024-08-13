package az.informix.ShopEasy.controller;

import az.informix.ShopEasy.model.Product;
import az.informix.ShopEasy.model.UserDtls;
import az.informix.ShopEasy.service.CategoryService;
import az.informix.ShopEasy.service.ProductService;
import az.informix.ShopEasy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
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
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session)throws IOException {
        String imageName = file.isEmpty() ? "default.png" : file.getOriginalFilename();
        user.setProfileImage(imageName);
        UserDtls saveUser = userService.saveUser(user);

        if(!ObjectUtils.isEmpty(saveUser)){
            if(!file.isEmpty()){
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("successMsg", "register successfully");
        }else{
            session.setAttribute("errorMsg", "something went wrong");
        }
        return "redirect:/register";
    }

}
