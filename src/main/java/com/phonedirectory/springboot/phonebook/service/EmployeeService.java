package com.phonedirectory.springboot.phonebook.service;

import com.phonedirectory.springboot.phonebook.model.EmployeeModel;

import com.phonedirectory.springboot.phonebook.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public List<EmployeeModel> getAllEmployees() {
        return repository.findAll();
    }

    public List<EmployeeModel> getEmployeeById(String id) {
        return repository.findByEmpId(id);
    }

    public List<EmployeeModel> getEmployeeByXname(String xname) {
        return repository.findByXname(xname);
    }

}
