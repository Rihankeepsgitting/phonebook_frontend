package com.phonedirectory.springboot.phonebook.repository;

import com.phonedirectory.springboot.phonebook.model.BusinessModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusinessRepository extends JpaRepository<BusinessModel, Integer> {
    @Query(value = "SELECT b from BusinessModel b WHERE CAST(b.business_id AS STRING) LIKE :business_id")
    List<BusinessModel> findByBusinessId(@Param("business_id") String business_id);
}
