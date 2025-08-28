package com.phonedirectory.springboot.phonebook.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business")
@Builder
public class BusinessModel {

    @Id
    @Column(name = "business_id")
    private int business_id;

    @Column(name = "bname")
    private String bname;

    @Column(name = "baddress")
    private String baddress;

    @Column(name = "bemail")
    private String bemail;

    @Column(name = "bphone")
    private String bphone;

    @Column(name = "bdescription")
    private String bdescription;
}
