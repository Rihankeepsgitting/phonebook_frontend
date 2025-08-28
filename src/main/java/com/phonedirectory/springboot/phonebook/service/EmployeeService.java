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

    public List<EmployeeModel> getAllEmployees(String business_id) {
        return repository.findByBusinessId(business_id);
    }

    public List<EmployeeModel> getEmployeeByAll(String query, String business_id) {
        Set<EmployeeModel> allSet = new HashSet<>();

        allSet.addAll(getEmployeeById(query, business_id));
        allSet.addAll(getEmployeeByXname(query, business_id));
        allSet.addAll(getEmployeeByPhone(query, business_id));
        allSet.addAll(getEmployeeByEmail(query, business_id));
        allSet.addAll(getEmployeeByDesignation(query, business_id));

        return new ArrayList<>(allSet);
    }

    public List<EmployeeModel> getEmployeeById(String id, String business_id) {
        return repository.findByEmpId(id, business_id);
    }

    public List<EmployeeModel> getEmployeeByXname(String xname, String business_id) { return repository.findByXname(xname, business_id); }

    public List<EmployeeModel> getEmployeeByPhone(String phone, String business_id) { return repository.findByPhone(phone, business_id); }

    public List<EmployeeModel> getEmployeeByEmail(String email, String business_id) { return repository.findByEmail(email, business_id); }

    public List<EmployeeModel> getEmployeeByDesignation(String designation, String business_id) { return repository.findByDesignation(designation, business_id); }

}
