package com.phonedirectory.springboot.phonebook.repository;

import com.phonedirectory.springboot.phonebook.model.UsersModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersModel, Integer> {

    @Query(value = "SELECT u FROM UsersModel u WHERE u.uname LIKE :uname")
    UsersModel findByUname(@Param("uname")String uname);

    @Query(value = "SELECT u FROM UsersModel u where cast(u.id as string) LIKE :id")
    Optional<UsersModel> findById(@Param("id")String id);

}
