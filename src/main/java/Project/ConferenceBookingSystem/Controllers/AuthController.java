package Project.ConferenceBookingSystem.Controllers;

import Project.ConferenceBookingSystem.Models.LoginRequest;
import Project.ConferenceBookingSystem.Models.User;
import Project.ConferenceBookingSystem.Repositories.UserRepository;
import Project.ConferenceBookingSystem.Services.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        // 1️⃣ Authenticate username + password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2️⃣ Fetch user from DB (TRUST DB, NOT CLIENT)
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3️⃣ Generate JWT using DB role
        return jwtUtil.generateToken(
                user.getUsername(),
                user.getRole()
        );
    }
}
