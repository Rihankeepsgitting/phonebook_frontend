package com.phonedirectory.springboot.phonebook.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder
public class UsersModel {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "business_id")
    private int business_id;

    @Column(name = "uname")
    private String uname;

    @Column(name = "upassword")
    private String upassword;

    @Column(name = "uroles")
    private String uroles;
}
