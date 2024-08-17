package az.informix.ShopEasy.service;

import az.informix.ShopEasy.model.UserDtls;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public UserDtls saveUser(UserDtls user);

    UserDtls getUserByEmail(String email);
}
