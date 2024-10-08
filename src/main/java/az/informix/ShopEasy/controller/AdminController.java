package az.informix.ShopEasy.controller;

import az.informix.ShopEasy.model.Category;
import az.informix.ShopEasy.model.Product;
import az.informix.ShopEasy.model.UserDtls;
import az.informix.ShopEasy.service.CategoryService;
import az.informix.ShopEasy.service.ProductService;
import az.informix.ShopEasy.service.UserService;
import jakarta.servlet.http.HttpSession;
import jdk.jfr.Registered;
import org.apache.catalina.LifecycleState;
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
import java.security.Principal;
import java.util.List;
import java.util.function.Predicate;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

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
    public String index(){
        return "admin/index";
    }
    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model model){
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);

        return "admin/add_product";
    }
    @GetMapping("/category")
    public String category(Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session)
    throws IOException {
        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);
        Boolean existCategory =  categoryService.existCategory(category.getName());
       if(existCategory){
           session.setAttribute("errorMsg", "Category Name already exists");
       }else{
           Category saveCategory = categoryService.saveCategory(category);
           if(ObjectUtils.isEmpty(saveCategory)){
               session.setAttribute("errorMsg", "Not saved : internal server error");
           }else{
               File saveFile = new ClassPathResource("static/img").getFile();
               Path path = Paths.get(saveFile.getAbsolutePath()+File.separator + "category_img"+File.separator+ file.getOriginalFilename());
               Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
               session.setAttribute("successMsg","Saved successfully");
           }
       }
        return "redirect:/admin/category";
    }
    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session){
        Boolean category = categoryService.deleteCategory(id);
        if(category){
            session.setAttribute("successMsg", "category deleted successfully");
        }else{
            session.setAttribute("errorMsg", "something wrong on server");
        }
        return "redirect:/admin/category";
    }
    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id, Model model){
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/edit_category";
    }
    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
                                 HttpSession session) throws IOException {

        Category oldCategory = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

        if (!ObjectUtils.isEmpty(category)) {

            oldCategory.setName(category.getName());
            oldCategory.setActive(category.isActive());
            oldCategory.setImageName(imageName);
        }

        Category updateCategory = categoryService.saveCategory(oldCategory);

        if (!ObjectUtils.isEmpty(updateCategory)) {

            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
                        + file.getOriginalFilename());

                // System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            session.setAttribute("successMsg", "Category update success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/loadEditCategory/" + category.getId();
    }
    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
                              HttpSession session) throws IOException {

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

        product.setImage(imageName);
        Product saveProduct = productService.saveProduct(product);

        if (!ObjectUtils.isEmpty(saveProduct)) {

            File saveFile = new ClassPathResource("static/img").getFile();

            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
                    + image.getOriginalFilename());

            // System.out.println(path);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            session.setAttribute("successMsg", "Product Saved Success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("/products")
    public String loadViewProduct(Model model){
       model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }
    @GetMapping("/editProduct/{id}")
    public String editProduct(Model model, @PathVariable int id){
        model.addAttribute("product", productService.findProductById(id));
        model.addAttribute("categories",categoryService.getAllCategory());
        return "admin/edit_product";
    }
    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, HttpSession session, @RequestParam("file")MultipartFile file)
    throws  Exception{
        Product oldProduct = productService.findProductById(product.getId());
        String imageName = file.isEmpty() ? oldProduct.getImage() : file.getOriginalFilename();

        if (!ObjectUtils.isEmpty(product)) {
            oldProduct.setTitle(product.getTitle());
            oldProduct.setStock(product.getStock());
            oldProduct.setDiscountPrice(product.getDiscountPrice());
            oldProduct.setDiscount(product.getDiscount());
            oldProduct.setDescription(product.getDescription());
            oldProduct.setPrice(product.getPrice());
            oldProduct.setCategory(product.getCategory());
            oldProduct.setIsActive(product.getIsActive());
            oldProduct.setImage(imageName);
        }
        Product updateProduct = productService.saveProduct(oldProduct);
        if (!ObjectUtils.isEmpty(updateProduct)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
                        + file.getOriginalFilename());
                // System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            session.setAttribute("successMsg", "Product update success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }
        return "redirect:/admin/editProduct/" + product.getId();
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id,HttpSession session){
        Boolean deletedProduct = productService.deleteProduct(id);
        if(deletedProduct){
            session.setAttribute("successMsg","Product delete successfully");
        }else{
            session.setAttribute("errorMsg", "something wrong on the server");
        }
        return "redirect:/admin/products";
    }
    @GetMapping("/users")
    public String getAllUser(Model model){
        List<UserDtls> users = userService.getUsers("ROLE_USER");
        model.addAttribute("users", users);
        return "admin/users";
    }
    @GetMapping("/updateSts")
    public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id, HttpSession session){
        Boolean f =userService.updateAccountStatus(id,status);
        if(f){
            session.setAttribute("successMsg", "Account Status Updated");
        }else{
            session.setAttribute("errorMsg", "Something went wrong");
        }
        return "redirect:/admin/users";
    }
}

