package com.phonedirectory.springboot.phonebook.service;


import com.phonedirectory.springboot.phonebook.model.BusinessModel;
import com.phonedirectory.springboot.phonebook.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    public List<BusinessModel> getAllBusiness(){
        return businessRepository.findAll();
    }

    public List<BusinessModel> findByBusinessId(@Param("business_id") String business_id) {
        return businessRepository.findByBusinessId(business_id);
    }
}
