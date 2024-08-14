package az.informix.ShopEasy.repository;

import az.informix.ShopEasy.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDtls, Integer> {
    public UserDtls findByEmail(String email);
}
