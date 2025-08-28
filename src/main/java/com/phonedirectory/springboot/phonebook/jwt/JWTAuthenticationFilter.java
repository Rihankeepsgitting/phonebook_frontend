package com.phonedirectory.springboot.phonebook.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Value("${security.enabled:true}")
    private boolean securityEnabled;

    private final JWTUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;

    public JWTAuthenticationFilter(JWTUtil jwtTokenUtil, ObjectMapper objectMapper) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.debug("Processing request to: {}", requestURI);

        // Skip filter for authentication and public endpoints
        if (shouldSkipAuthentication(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        if (!securityEnabled) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                chain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            String username = jwtTokenUtil.extractUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtTokenUtil.validateToken(token)) {
                    // Extract authorities (roles) from token - you need to implement this
                    List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            chain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            handleJwtException(response, "Token has expired", HttpStatus.UNAUTHORIZED, ex);
        } catch (SignatureException | MalformedJwtException ex) {
            handleJwtException(response, "Invalid token", HttpStatus.UNAUTHORIZED, ex);
        } catch (Exception ex) {
            handleJwtException(response, "Authentication error", HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    private boolean shouldSkipAuthentication(String requestURI) {
        return requestURI.startsWith("/auth/");
    }

    private void handleJwtException(HttpServletResponse response, String message,
                                    HttpStatus status, Exception ex) throws IOException {
        log.error("JWT Error: {}", message, ex);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("message", ex.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}