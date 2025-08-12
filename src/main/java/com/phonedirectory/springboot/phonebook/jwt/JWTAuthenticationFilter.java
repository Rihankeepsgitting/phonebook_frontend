package com.phonedirectory.springboot.phonebook.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Configuration
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

        if (!securityEnabled) {
            chain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        try {
            if (authHeader == null || authHeader.isBlank()) {
                // no Authorization header -> proceed as anonymous (controller/security will block protected endpoints)
                chain.doFilter(request, response);
                return;
            }

            if (!authHeader.startsWith("Bearer ")) {
                // not a bearer token -> proceed (or optionally reject)
                chain.doFilter(request, response);
                return;
            }

            token = authHeader.substring(7);
            username = jwtTokenUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                String roleClaim = jwtTokenUtil.extractRoles(token);
                if (roleClaim == null || roleClaim.isBlank()) {
                    // default role if none provided
                    roleClaim = "USER";
                }

                // support comma-separated roles
                List<GrantedAuthority> authorities = Arrays.stream(roleClaim.split(","))
                        .map(String::trim)
                        .filter(r -> !r.isEmpty())
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            log.info("Expired JWT", ex);
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token has expired. Please log in again.");
        } catch (SignatureException | MalformedJwtException ex) {
            log.info("Invalid JWT", ex);
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid token. Please log in again.");
        } catch (Exception ex) {
            log.error("Unexpected error in JWT filter", ex);
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        String json = objectMapper.writeValueAsString(error);

        response.getWriter().write(json);
    }
}
