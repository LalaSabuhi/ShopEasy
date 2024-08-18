package az.informix.ShopEasy.controller;

import az.informix.ShopEasy.model.Category;
import az.informix.ShopEasy.model.UserDtls;
import az.informix.ShopEasy.service.CategoryService;
import az.informix.ShopEasy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @ModelAttribute
    public void getUser(Principal p, Model model){
        if(p != null){
            String email = p.getName();
            UserDtls user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
        }
        List<Category> allActiveCategory = categoryService.getAllCategory();
        model.addAttribute("categorys", allActiveCategory);
    }
    @GetMapping("/")
    public String home() {
        return "user/home";
    }
}
