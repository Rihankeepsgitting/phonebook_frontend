package com.phonedirectory.springboot.phonebook.service;

import com.phonedirectory.springboot.phonebook.model.UsersModel;
import com.phonedirectory.springboot.phonebook.repository.UsersRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository repository;

    public CustomUserDetailsService(UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersModel user = repository.findByUname(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.builder()
                .username(user.getUname())
                .password(user.getUpassword()) // must be BCrypt encoded
                .build();
    }
}
