package com.project.jobportal.service;

import com.project.jobportal.dto.RecruiterJobsDto;
import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.interfaces.IRecruiterJobs;
import com.project.jobportal.repository.JobCompanyRepository;
import com.project.jobportal.repository.JobLocationRepository;
import com.project.jobportal.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;

    private final JobLocationRepository jobLocationRepository;

    private final JobCompanyRepository jobCompanyRepository;

    @Autowired
    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository, JobLocationRepository jobLocationRepository, JobCompanyRepository jobCompanyRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
        this.jobLocationRepository = jobLocationRepository;
        this.jobCompanyRepository = jobCompanyRepository;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity){
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<RecruiterJobsDto> getRecruiterJobs(int recruiter){
        List<IRecruiterJobs> recruiterJobsDto = jobPostActivityRepository.getRecruiterJobs(recruiter);

        List<RecruiterJobsDto> recruiterJobs = new ArrayList<>();

        for (IRecruiterJobs rec: recruiterJobsDto)  {
            RecruiterJobsDto jobsDto = new RecruiterJobsDto();
            jobsDto.setTotalCandidates(rec.getTotalCandidates());
            jobsDto.setJobPostId(rec.getJob_post_id());
            jobsDto.setJobTitle(rec.getJob_title());

            jobsDto.setJobLocationId(jobLocationRepository.findById(rec.getLocationId()).get());
            jobsDto.setJobCompanyId(jobCompanyRepository.findById(rec.getCompanyId()).get());
            recruiterJobs.add(jobsDto);
        }
        return recruiterJobs;
    }

    public JobPostActivity getOne(int id) {
        return jobPostActivityRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
    }
}
