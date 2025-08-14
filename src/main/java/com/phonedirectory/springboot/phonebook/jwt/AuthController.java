package com.phonedirectory.springboot.phonebook.jwt;

import com.phonedirectory.springboot.phonebook.model.ApiError;
import com.phonedirectory.springboot.phonebook.model.UsersModel;
import com.phonedirectory.springboot.phonebook.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UsersService service;

    public AuthController(
            JWTUtil jwtUtil,
            AuthenticationManager authenticationManager,
            UsersService service
    ) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String uname = request.get("uname");
        String upassword = request.get("upassword");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(uname, upassword)
            );

            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            UsersModel user = service.findByUname(username);

            if (user == null) {
                log.warn("Authentication succeeded but user record not found for username: {}", username);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiError("Internal error: user record not found"));
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());

            String token = jwtUtil.generateTokenWithClaims(username, claims);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            log.info("Authentication failed for username: {} - {}", uname, ex.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError("Invalid credentials. Check your username and password."));
        } catch (Exception ex) {
            log.error("Unexpected error during login", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError("Internal server error"));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || authHeader.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError("Authorization header missing"));
            }

            String token = authHeader;
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError("Invalid token"));
            }

            String username = jwtUtil.extractUsername(token);
            UsersModel user = service.findByUname(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError("User not found"));
            }

            return ResponseEntity.ok(user);
        } catch (Exception ex) {
            log.error("Token validation failed", ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError("Token validation failed"));
        }
    }
}
