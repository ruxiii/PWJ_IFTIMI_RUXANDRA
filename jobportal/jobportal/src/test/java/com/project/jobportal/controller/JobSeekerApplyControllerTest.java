package com.project.jobportal.controller;

import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.entity.JobSeekerApply;
import com.project.jobportal.entity.JobSeekerSave;
import com.project.jobportal.service.JobPostActivityService;
import com.project.jobportal.service.JobSeekerApplyService;
import com.project.jobportal.service.JobSeekerSaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JobSeekerApplyControllerTest {

    @Mock
    private JobPostActivityService jobPostActivityService;

    @Mock
    private JobSeekerApplyService jobSeekerApplyService;

    @Mock
    private JobSeekerSaveService jobSeekerSaveService;

    @Mock
    private Model model;

    @InjectMocks
    private JobSeekerApplyController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDisplay() {
        int jobId = 1;
        JobPostActivity jobDetails = new JobPostActivity();
        List<JobSeekerApply> jobSeekerApplyList = new ArrayList<>();
        List<JobSeekerSave> jobSeekerSaveList = new ArrayList<>();

        when(jobPostActivityService.getOne(jobId)).thenReturn(jobDetails);
        when(jobSeekerApplyService.getJobCandidates(jobDetails)).thenReturn(jobSeekerApplyList);
        when(jobSeekerSaveService.getJobCandidates(jobDetails)).thenReturn(jobSeekerSaveList);

        String viewName = controller.display(jobId, model);

        verify(jobPostActivityService, times(1)).getOne(jobId);
        verify(jobSeekerApplyService, times(1)).getJobCandidates(jobDetails);
        verify(jobSeekerSaveService, times(1)).getJobCandidates(jobDetails);
        verify(jobSeekerApplyService, times(1)).display(eq(jobId), eq(model), eq(jobDetails), eq(jobSeekerApplyList), eq(jobSeekerSaveList));

        assertEquals("job-details", viewName);
    }

    @Test
    void testApply() {
        int jobId = 1;
        JobSeekerApply jobSeekerApply = new JobSeekerApply();
        JobPostActivity jobPostActivity = new JobPostActivity();

        when(jobPostActivityService.getOne(jobId)).thenReturn(jobPostActivity);

        String viewName = controller.apply(jobId, jobSeekerApply);

        verify(jobPostActivityService, times(1)).getOne(jobId);
        verify(jobSeekerApplyService, times(1)).addNew(eq(jobId), eq(jobSeekerApply), eq(jobPostActivity));

        assertEquals("redirect:/dashboard/", viewName);
    }
}
