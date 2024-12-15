package com.project.jobportal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "job_location")
@Data
public class JobLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String city;

    private String state;

    private String country;
}
