package recipes.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class UserService implements UserDetailsService {
    UserRepo repo;

    @Autowired
    public UserService(UserRepo repo) {
        this.repo = repo;
    }

    public long newUser(User user) {
        repo.save(user);
        return user.getId();
    }

    public boolean emailExists(User user) {
        return repo.existsByEmail(user.email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findFirstByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }
        return user;
    }
}
