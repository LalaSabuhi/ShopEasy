package az.informix.ShopEasy.service;

import az.informix.ShopEasy.model.UserDtls;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public UserDtls saveUser(UserDtls user);

    UserDtls getUserByEmail(String email);
   List<UserDtls> getUsers(String role);
   Boolean updateAccountStatus(Integer id, Boolean status);

   public void increaseFailedAttempt(UserDtls user);
   public void userAccountLock(UserDtls user);
   public boolean unlockAccountTimeExpired(UserDtls user);
   public void resetAttempt(int userId);
}
