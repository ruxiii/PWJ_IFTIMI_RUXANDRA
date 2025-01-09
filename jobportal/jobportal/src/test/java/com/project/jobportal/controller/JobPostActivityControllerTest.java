package com.project.jobportal.controller;

import com.project.jobportal.controller.JobPostActivityController;
import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.service.JobPostActivityService;
import com.project.jobportal.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JobPostActivityControllerTest {

    @Mock
    private UsersService usersService;

    @Mock
    private JobPostActivityService jobPostActivityService;

    @Mock
    private Model model;

    @InjectMocks
    private JobPostActivityController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchJobs() {
        String viewName = controller.searchJobs(model, "Developer", "New York", null, "true", null, null, null, null, true, false, false);

        verify(jobPostActivityService, times(1)).searchJobs(
                eq("Developer"), eq("New York"), eq(null), eq("true"), eq(null),
                eq(null), eq(null), eq(null), eq(true), eq(false), eq(false), eq(model)
        );
        assertEquals("dashboard", viewName);
    }

    @Test
    void testAddJobs() {
        when(usersService.getCurrentUserProfile()).thenReturn(new Object()); // Mock user profile

        String viewName = controller.addJobs(model);

        verify(model, times(1)).addAttribute(eq("jobPostActivity"), any(JobPostActivity.class));
        verify(model, times(1)).addAttribute(eq("user"), any());
        assertEquals("add-jobs", viewName);
    }

    @Test
    void testAddNew() {
        JobPostActivity jobPostActivity = new JobPostActivity();
        JobPostActivity savedJobPostActivity = new JobPostActivity();

        when(jobPostActivityService.addNew(jobPostActivity)).thenReturn(savedJobPostActivity);

        String viewName = controller.addNew(jobPostActivity, model);

        verify(jobPostActivityService, times(1)).addNew(jobPostActivity);
        verify(model, times(1)).addAttribute("jobPostActivity", savedJobPostActivity);
        assertEquals("redirect:/dashboard/", viewName);
    }

    @Test
    void testEditJob() {
        int jobId = 1;
        JobPostActivity jobPostActivity = new JobPostActivity();

        when(jobPostActivityService.getOne(jobId)).thenReturn(jobPostActivity);
        when(usersService.getCurrentUserProfile()).thenReturn(new Object());

        String viewName = controller.editJob(jobId, model);

        verify(jobPostActivityService, times(1)).getOne(jobId);
        verify(model, times(1)).addAttribute("jobPostActivity", jobPostActivity);
        verify(model, times(1)).addAttribute(eq("user"), any());
        assertEquals("add-jobs", viewName);
    }

    @Test
    void testGlobalSearch() {
        String viewName = controller.globalSearch(model, "Tester", "Remote", null, null, null, "true", null, null, false, true, false);

        verify(jobPostActivityService, times(1)).globalSearch(
                eq("Tester"), eq("Remote"), eq(null), eq(null), eq(null),
                eq("true"), eq(null), eq(null), eq(false), eq(true), eq(false), eq(model)
        );
        assertEquals("global-search", viewName);
    }
}

