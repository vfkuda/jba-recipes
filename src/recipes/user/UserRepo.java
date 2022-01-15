package recipes.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    boolean existsByEmail(String email);
    User findFirstByEmail(String username);
}
