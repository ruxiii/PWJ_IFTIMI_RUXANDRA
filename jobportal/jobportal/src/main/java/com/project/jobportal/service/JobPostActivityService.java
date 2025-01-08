package com.project.jobportal.service;

import com.project.jobportal.dto.RecruiterJobsDto;
import com.project.jobportal.entity.*;
import com.project.jobportal.interfaces.IRecruiterJobs;
import com.project.jobportal.repository.JobCompanyRepository;
import com.project.jobportal.repository.JobLocationRepository;
import com.project.jobportal.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.*;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;

    private final JobLocationRepository jobLocationRepository;

    private final JobCompanyRepository jobCompanyRepository;

    private final UsersService usersService;

    private final JobSeekerSaveService jobSeekerSaveService;

    @Lazy
    private final JobSeekerApplyService jobSeekerApplyService;

    @Autowired
    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository, JobLocationRepository jobLocationRepository, JobCompanyRepository jobCompanyRepository, UsersService usersService, JobSeekerSaveService jobSeekerSaveService, JobSeekerApplyService jobSeekerApplyService) {
        this.jobPostActivityRepository = jobPostActivityRepository;
        this.jobLocationRepository = jobLocationRepository;
        this.jobCompanyRepository = jobCompanyRepository;
        this.usersService = usersService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.jobSeekerApplyService = jobSeekerApplyService;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity){
        Users user = usersService.getCurrentUser();
        if(user!=null){
            jobPostActivity.setPostedById(user);
        }
        jobPostActivity.setPostedDate(new Date());
        jobPostActivityRepository.save(jobPostActivity);
        return jobPostActivity;
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

    public List<JobPostActivity> getAll() {
        return jobPostActivityRepository.findAll();
    }

    public List<JobPostActivity> search(String job, String location, List<String> type, List<String> remote, LocalDate searchDate) {
        return Objects.isNull(searchDate) ?
                jobPostActivityRepository.searchWithoutDate(job, location, remote, type) :
                jobPostActivityRepository.search(job, location, remote, type, searchDate);
    }

    public List<JobPostActivity> globalSearch(String job, String location, String partTime, String fullTime,
                                              String freelance, String remoteOnly, String officeOnly, String partialRemote,
                                              boolean today, boolean days7, boolean days30, Model model) {

        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(partTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(partTime, "Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(partTime, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(partTime, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partTime, "Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;
        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;

        if (days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (today) {
            searchDate = LocalDate.now();
        } else {
            dateSearchFlag = false;
        }

        if (partTime == null && fullTime == null && freelance == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote = false;
        }

        if (officeOnly == null && remoteOnly == null && partialRemote == null) {
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            type = false;
        }

        if (!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)) {
            jobPost = getAll();
        } else {
            jobPost = search(job, location, Arrays.asList(partTime, fullTime, freelance),
                    Arrays.asList(remoteOnly, officeOnly, partialRemote), searchDate);
        }

        model.addAttribute("jobPost", jobPost);

        return jobPost;

    }

    public List<JobPostActivity> searchJobs(String job, String location, String partTime, String fullTime, String freelance,
                                            String remoteOnly, String officeOnly, String partialRemote,
                                            boolean today, boolean days7, boolean days30, Model model) {

        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(freelance, "Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;

        if (days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (today) {
            searchDate = LocalDate.now();
        } else {
            dateSearchFlag = false;
        }

        if (partTime == null && fullTime == null && freelance == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote = false;
        }

        if (remoteOnly == null && officeOnly == null && partialRemote == null) {
            remoteOnly = "Remote-Only";
            officeOnly = "Office-Only";
            partialRemote = "Partial-Remote";
            type = false;
        }

        List<JobPostActivity> jobPosts;
        if (!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)) {
            jobPosts = getAll();
        } else {
            jobPosts = search(job, location, Arrays.asList(partTime, fullTime, freelance),
                    Arrays.asList(remoteOnly, officeOnly, partialRemote), searchDate);
        }

        Object currentUserProfile = usersService.getCurrentUserProfile();
        model.addAttribute("user", currentUserProfile);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            model.addAttribute("username", currentUserName);

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                List<RecruiterJobsDto> recruiterJobs = getRecruiterJobs(((RecruiterProfile) currentUserProfile).getUserAccountId());
                model.addAttribute("jobPost", recruiterJobs);
            } else if (currentUserProfile instanceof JobSeekerProfile) {
                List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getCandidatesJobs((JobSeekerProfile) currentUserProfile);
                List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) currentUserProfile);

                for (JobPostActivity jobActivity : jobPosts) {
                    boolean isApplied = jobSeekerApplyList.stream()
                            .anyMatch(apply -> Objects.equals(jobActivity.getJobPostId(), apply.getJob().getJobPostId()));
                    boolean isSaved = jobSeekerSaveList.stream()
                            .anyMatch(save -> Objects.equals(jobActivity.getJobPostId(), save.getJob().getJobPostId()));

                    jobActivity.setIsActive(isApplied);
                    jobActivity.setIsSaved(isSaved);
                }

                model.addAttribute("jobPost", jobPosts);
            }
        }

        return jobPosts;
    }
}