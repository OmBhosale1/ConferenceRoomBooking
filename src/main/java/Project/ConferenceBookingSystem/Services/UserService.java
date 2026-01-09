package Project.ConferenceBookingSystem.Services;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Project.ConferenceBookingSystem.Models.User;
import Project.ConferenceBookingSystem.Repositories.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void registerUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setEnabled(true);

        userRepository.save(user);
    }

    public List<User> showAllUsers() {
        return userRepository.findAll();
    }
}

