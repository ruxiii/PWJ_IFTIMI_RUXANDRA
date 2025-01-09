package com.project.jobportal.controller;

import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.UsersRepository;
import com.project.jobportal.service.RecruiterProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecruiterProfileControllerTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RecruiterProfileService recruiterProfileService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Model model;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private RecruiterProfileController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testRecruiterProfile() {
        String currentUsername = "test@example.com";
        Users user = new Users();
        user.setUserId(1);
        RecruiterProfile recruiterProfile = new RecruiterProfile();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(currentUsername);
        when(usersRepository.findByEmail(currentUsername)).thenReturn(Optional.of(user));
        when(recruiterProfileService.getOne(user.getUserId())).thenReturn(Optional.of(recruiterProfile));

        String viewName = controller.recruiterProfile(model);

        verify(usersRepository, times(1)).findByEmail(currentUsername);
        verify(recruiterProfileService, times(1)).getOne(user.getUserId());
        verify(model, times(1)).addAttribute("profile", recruiterProfile);
        assertEquals("recruiter-profile", viewName);
    }

    @Test
    void testRecruiterProfile_AnonymousUser() {
        when(securityContext.getAuthentication()).thenReturn(mock(AnonymousAuthenticationToken.class));

        String viewName = controller.recruiterProfile(model);

        verifyNoInteractions(usersRepository, recruiterProfileService);
        verifyNoInteractions(model);
        assertEquals("recruiter-profile", viewName);
    }

    @Test
    void testAddNew() throws IOException {
        RecruiterProfile recruiterProfile = new RecruiterProfile();

        String viewName = controller.addNew(recruiterProfile, multipartFile, model);

        verify(recruiterProfileService, times(1)).addNew(eq(recruiterProfile), eq(multipartFile), eq(model));
        assertEquals("redirect:/dashboard/", viewName);
    }
}

