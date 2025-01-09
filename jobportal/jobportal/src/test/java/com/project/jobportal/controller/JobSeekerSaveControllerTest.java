package com.project.jobportal.controller;

import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.entity.JobSeekerSave;
import com.project.jobportal.entity.Users;
import com.project.jobportal.service.JobPostActivityService;
import com.project.jobportal.service.JobSeekerProfileService;
import com.project.jobportal.service.JobSeekerSaveService;
import com.project.jobportal.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JobSeekerSaveControllerTest {

    @Mock
    private UsersService usersService;

    @Mock
    private JobSeekerProfileService jobSeekerProfileService;

    @Mock
    private JobPostActivityService jobPostActivityService;

    @Mock
    private JobSeekerSaveService jobSeekerSaveService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Model model;

    @InjectMocks
    private JobSeekerSaveController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testSave() {
        int jobId = 1;
        String currentUsername = "test@example.com";
        Users user = new Users();
        user.setUserId(1);
        JobSeekerProfile seekerProfile = new JobSeekerProfile();
        JobPostActivity jobPostActivity = new JobPostActivity();
        JobSeekerSave jobSeekerSave = new JobSeekerSave();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(currentUsername);
        when(usersService.findByEmail(currentUsername)).thenReturn(user);
        when(jobSeekerProfileService.getOne(user.getUserId())).thenReturn(Optional.of(seekerProfile));
        when(jobPostActivityService.getOne(jobId)).thenReturn(jobPostActivity);

        String viewName = controller.save(jobId, jobSeekerSave);

        verify(jobSeekerSaveService, times(1)).addNew(any(JobSeekerSave.class));
        assertEquals("redirect:/dashboard/", viewName);
    }

    @Test
    void testSave_AnonymousUser() {
        when(securityContext.getAuthentication()).thenReturn(mock(AnonymousAuthenticationToken.class));

        String viewName = controller.save(1, new JobSeekerSave());

        verifyNoInteractions(usersService, jobSeekerProfileService, jobPostActivityService, jobSeekerSaveService);
        assertEquals("redirect:/dashboard/", viewName);
    }

    @Test
    void testSaveThrowsRuntimeExceptionWhenUserNotFound() {
        SecurityContext mockSecurityContext = Mockito.mock(SecurityContext.class);
        Authentication mockAuthentication = Mockito.mock(Authentication.class);

        SecurityContextHolder.setContext(mockSecurityContext);

        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getName()).thenReturn("testuser");

        Users mockUser = new Users();
        mockUser.setUserId(1);
        when(usersService.findByEmail("testuser")).thenReturn(mockUser);

        when(jobSeekerProfileService.getOne(mockUser.getUserId())).thenReturn(Optional.empty());

        when(jobPostActivityService.getOne(Mockito.anyInt())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            controller.save(1, new JobSeekerSave());
        });

        assertEquals("User not found", exception.getMessage());
    }



    @Test
    void testSavedJobs() {
        JobSeekerProfile currentUserProfile = new JobSeekerProfile();
        List<JobSeekerSave> jobSeekerSaveList = new ArrayList<>();
        List<JobPostActivity> jobPostList = new ArrayList<>();

        JobPostActivity jobPostActivity = new JobPostActivity();
        JobSeekerSave jobSeekerSave = new JobSeekerSave();
        jobSeekerSave.setJob(jobPostActivity);
        jobSeekerSaveList.add(jobSeekerSave);
        jobPostList.add(jobPostActivity);

        when(usersService.getCurrentUserProfile()).thenReturn(currentUserProfile);
        when(jobSeekerSaveService.getCandidatesJob(currentUserProfile)).thenReturn(jobSeekerSaveList);

        String viewName = controller.savedJobs(model);

        verify(model, times(1)).addAttribute("jobPost", jobPostList);
        verify(model, times(1)).addAttribute("user", currentUserProfile);
        assertEquals("saved-jobs", viewName);
    }
}
