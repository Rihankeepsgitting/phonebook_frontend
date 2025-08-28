package com.phonedirectory.springboot.phonebook.jwt;

import com.phonedirectory.springboot.phonebook.model.ApiError;
import com.phonedirectory.springboot.phonebook.model.UsersModel;
import com.phonedirectory.springboot.phonebook.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final UsersRepository repository;

    public AuthController(JWTUtil jwtUtil,
                          AuthenticationManager authenticationManager,
                          UsersRepository repository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.repository = repository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String uname = request.get("uname");
        String upassword = request.get("upassword");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(uname, upassword)
            );

            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            UsersModel user = repository.findByUname(username);

            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("business_id", user.getBusiness_id());
            claims.put("uname", user.getUname());
            claims.put("uroles", user.getUroles());

            String token = jwtUtil.generateTokenWithClaims(username, claims);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            log.info("Authentication failed for username: {} - {}", uname, ex.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError("Invalid credentials"));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || authHeader.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError("Authorization header missing"));
            }

            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError("Invalid token"));
            }

            String username = jwtUtil.extractUsername(token);
            UsersModel user = repository.findByUname(username);

            return ResponseEntity.ok(user);
        } catch (Exception ex) {
            log.error("Token validation failed", ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError("Token validation failed"));
        }
    }
}
