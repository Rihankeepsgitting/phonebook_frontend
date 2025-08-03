package com.phonedirectory.springboot.phonebook.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
@Builder
public class EmployeeModel {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "business_id")
    private int business_id;

    @Column(name = "xname")
    private String xname;

    @Column(name = "designation")
    private String designation;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

}
