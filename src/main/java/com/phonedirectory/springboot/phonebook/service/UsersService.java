package com.phonedirectory.springboot.phonebook.service;

import com.phonedirectory.springboot.phonebook.model.UsersModel;
import com.phonedirectory.springboot.phonebook.repository.UsersRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository repository;
    private final EntityManager entityManager;

    public UsersService(
            UsersRepository repository,
            EntityManager entityManager
    ) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    public Optional<UsersModel> findById(String id) {
        return repository.findById(id);
    }

    public UsersModel findByUname(String uname) {
        return repository.findByUname(uname);
    }
}
