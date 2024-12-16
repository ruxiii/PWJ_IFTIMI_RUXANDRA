package com.project.jobportal.repository;

import com.project.jobportal.entity.JobLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobLocationRepository extends JpaRepository<JobLocation, Integer> {
}
