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
import java.util.*;
import java.util.stream.Collectors;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public List<EmployeeModel> getAllEmployees() {
        return repository.findAll();
    }

    public List<EmployeeModel> getEmployeeByAll(String query) {
        Set<EmployeeModel> allSet = new HashSet<>();

        allSet.addAll(getEmployeeById(query));
        allSet.addAll(getEmployeeByXname(query));
        allSet.addAll(getEmployeeByPhone(query));
        allSet.addAll(getEmployeeByEmail(query));
        allSet.addAll(getEmployeeByDesignation(query));

        return new ArrayList<>(allSet);
    }

    public List<EmployeeModel> getEmployeeById(String id) {
        return repository.findByEmpId(id);
    }

    public List<EmployeeModel> getEmployeeByXname(String xname) { return repository.findByXname(xname); }

    public List<EmployeeModel> getEmployeeByPhone(String phone) { return repository.findByPhone(phone); }

    public List<EmployeeModel> getEmployeeByEmail(String email) { return repository.findByEmail(email); }

    public List<EmployeeModel> getEmployeeByDesignation(String designation) { return repository.findByDesignation(designation); }

}
