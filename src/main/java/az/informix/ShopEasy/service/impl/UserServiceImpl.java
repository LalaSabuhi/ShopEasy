package az.informix.ShopEasy.service.impl;

import az.informix.ShopEasy.model.UserDtls;
import az.informix.ShopEasy.repository.UserRepository;
import az.informix.ShopEasy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDtls saveUser(UserDtls user) {
        user.setRole("ROLE_USER");
        String encodePassword= passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        UserDtls saveUser =  userRepository.save(user);
        return saveUser;
    }
}
