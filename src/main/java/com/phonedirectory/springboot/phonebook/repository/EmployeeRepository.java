package com.phonedirectory.springboot.phonebook.repository;

import com.phonedirectory.springboot.phonebook.model.EmployeeModel;
import com.phonedirectory.springboot.phonebook.model.UsersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeModel, Integer> {
    @Query(value = "SELECT e FROM EmployeeModel e WHERE CAST(e.business_id AS STRING) LIKE CONCAT('%', :business_id, '%')")
    List<EmployeeModel> findByBusinessId(@Param("business_id") String business_id);

    @Query(value = "SELECT e FROM EmployeeModel e WHERE e.id NOT IN (SELECT u.id FROM UsersModel u)")
    List<EmployeeModel> findByUserNotIn();

    @Query(value = "SELECT e FROM EmployeeModel e WHERE e.id IN (SELECT u.id FROM UsersModel u)")
    List<EmployeeModel> findByUserIn();

    @Query(value = "SELECT e FROM EmployeeModel e WHERE LOWER(e.xname) LIKE LOWER(CONCAT(:xname, '%')) AND CAST(e.business_id as STRING) LIKE CONCAT('%', :business_id, '%') " +
    "OR LOWER(e.xname) LIKE LOWER(CONCAT('% ', :xname, '%')) AND CAST(e.business_id as STRING) LIKE CONCAT('%', :business_id, '%')")
    List<EmployeeModel> findByXname(@Param("xname") String xname, @Param("business_id") String business_id);

    @Query(value = "SELECT e FROM EmployeeModel e WHERE LOWER(e.email) LIKE LOWER(CONCAT(:email, '%')) AND CAST(e.business_id as STRING) LIKE CONCAT('%', :business_id, '%') " +
            "OR LOWER(e.email) LIKE LOWER(CONCAT('% ', :email, '%')) AND CAST(e.business_id as STRING) LIKE CONCAT('%', :business_id, '%')")
    List<EmployeeModel> findByEmail(@Param("email") String email, @Param("business_id") String business_id);

    @Query(value = "SELECT e FROM EmployeeModel e WHERE LOWER(e.phone) LIKE CONCAT('%', :phone, '%') AND CAST(e.business_id as STRING) LIKE CONCAT('%', :business_id, '%') ")
    List<EmployeeModel> findByPhone(@Param("phone") String phone, @Param("business_id") String business_id);

    @Query(value = "SELECT e FROM EmployeeModel e WHERE LOWER(e.designation) LIKE LOWER(CONCAT(:designation, '%')) AND CAST(e.business_id as STRING) LIKE CONCAT('%', :business_id, '%') " +
            "OR LOWER(e.designation) LIKE LOWER(CONCAT('% ', :designation, '%')) AND CAST(e.business_id as STRING) LIKE CONCAT('%', :business_id, '%')")
    List<EmployeeModel> findByDesignation(@Param("designation") String designation, @Param("business_id") String business_id);

    @Query(value = "SELECT e FROM EmployeeModel e WHERE cast(e.id AS STRING) LIKE CONCAT('%' ,:id, '%') AND CAST(e.business_id as STRING) LIKE CONCAT('%', :business_id, '%') ")
    List<EmployeeModel> findByEmpId(@Param("id") String id, @Param("business_id") String business_id);
}
