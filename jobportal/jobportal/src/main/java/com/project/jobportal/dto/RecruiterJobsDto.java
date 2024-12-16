package com.project.jobportal.dto;

import com.project.jobportal.entity.JobCompany;
import com.project.jobportal.entity.JobLocation;
import lombok.Data;

@Data
public class RecruiterJobsDto {
    private Long totalCandidates;

    private Integer jobPostId;

    private String jobTitle;

    private JobLocation jobLocationId;

    private JobCompany jobCompanyId;
}
