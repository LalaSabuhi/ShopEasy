package az.informix.ShopEasy.service.impl;

import az.informix.ShopEasy.model.UserDtls;
import az.informix.ShopEasy.repository.UserRepository;
import az.informix.ShopEasy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDtls saveUser(UserDtls user) {
        UserDtls saveUser =  userRepository.save(user);
        return saveUser;
    }
}
