package com.project.jobportal.repository;

import com.project.jobportal.entity.JobCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCompanyRepository extends JpaRepository<JobCompany, Integer> {
}
