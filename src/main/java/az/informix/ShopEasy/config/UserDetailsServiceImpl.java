package az.informix.ShopEasy.config;

import az.informix.ShopEasy.config.CustomUser;
import az.informix.ShopEasy.model.UserDtls;
import az.informix.ShopEasy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDtls user = userRepository.findByEmail(username);
        if(user == null){
            throw  new UsernameNotFoundException("Username not found");
        }
        return new CustomUser(user);
    }
}
