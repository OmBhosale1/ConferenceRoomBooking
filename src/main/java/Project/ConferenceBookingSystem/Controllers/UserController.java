package Project.ConferenceBookingSystem.Controllers;

import org.springframework.web.bind.annotation.*;

import Project.ConferenceBookingSystem.Models.User;
import Project.ConferenceBookingSystem.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        userService.registerUser(user);
        return "User registered successfully";
    }

    @GetMapping("showAllUsers")
    public List<User> showAllUsers() {
        return userService.showAllUsers();
    }
}
