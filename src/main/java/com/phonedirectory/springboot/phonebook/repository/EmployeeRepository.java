package com.phonedirectory.springboot.phonebook.repository;

import com.phonedirectory.springboot.phonebook.model.EmployeeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeModel, Integer> {
    @Query(value = "SELECT e FROM EmployeeModel e WHERE LOWER(e.xname) LIKE LOWER(CONCAT(:xname, '%'))" +
    "OR LOWER(e.xname) LIKE LOWER(CONCAT('% ', :xname, '%'))")
    List<EmployeeModel> findByXname(@Param("xname") String xname);

    @Query(value = "SELECT e FROM EmployeeModel e where cast(e.id as string) LIKE concat(:id, '%')")
    List<EmployeeModel> findByEmpId(@Param("id") String id);
}
