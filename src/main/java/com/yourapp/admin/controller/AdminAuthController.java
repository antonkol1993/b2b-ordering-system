package com.yourapp.admin.controller;

import com.yourapp.session.SessionService;
import com.yourapp.common.AppConstants;
import com.yourapp.user.entity.User;
import com.yourapp.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminAuthController {

    private final UserRepository userRepository;
    private final SessionService sessionService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AdminLoginResponse login(@RequestBody AdminLoginRequest request, HttpSession session) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!AppConstants.ROLE_ADMIN.equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not an admin");
        }
        if (user.getPassword() == null || !user.getPassword().equals(request.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        sessionService.setSession(session, user);
        return new AdminLoginResponse(user.getId(), user.getTenant().getSlug());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpSession session) {
        sessionService.clear(session);
    }

    public record AdminLoginRequest(String email, String password) {}

    public record AdminLoginResponse(Long userId, String tenantSlug) {}
}

