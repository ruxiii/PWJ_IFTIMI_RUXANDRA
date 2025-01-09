package com.project.jobportal.service;

import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import com.project.jobportal.entity.UsersType;
import com.project.jobportal.repository.JobSeekerProfileRepository;
import com.project.jobportal.repository.RecruiterProfileRepository;
import com.project.jobportal.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RecruiterProfileRepository recruiterProfileRepository;

    @Mock
    private JobSeekerProfileRepository jobSeekerProfileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewUser_recruiter() {
        Users user = new Users();
        UsersType usersType = new UsersType();
        usersType.setUserTypeId(1);
        user.setPassword("plainPassword");
        user.setUserTypeId(usersType);

        Users savedUser = new Users();
        savedUser.setUserId(1);

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(usersRepository.save(user)).thenReturn(savedUser);

        Users result = usersService.addNew(user);

        assertNotNull(result);
        assertEquals(savedUser, result);
        assertEquals("encodedPassword", user.getPassword());
        verify(recruiterProfileRepository, times(1)).save(any(RecruiterProfile.class));
        verify(jobSeekerProfileRepository, never()).save(any(JobSeekerProfile.class));
    }

    @Test
    void testAddNewUser_jobSeeker() {
        Users user = new Users();
        UsersType usersType = new UsersType();
        usersType.setUserTypeId(2);
        user.setPassword("plainPassword");
        user.setUserTypeId(usersType);

        Users savedUser = new Users();
        savedUser.setUserId(1);

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(usersRepository.save(user)).thenReturn(savedUser);

        Users result = usersService.addNew(user);

        assertNotNull(result);
        assertEquals(savedUser, result);
        assertEquals("encodedPassword", user.getPassword());
        verify(jobSeekerProfileRepository, times(1)).save(any(JobSeekerProfile.class));
        verify(recruiterProfileRepository, never()).save(any(RecruiterProfile.class));
    }

    @Test
    void testGetUserByEmail() {
        String email = "test@example.com";
        Users user = new Users();
        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<Users> result = usersService.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetCurrentUserProfile_AuthenticatedJobSeeker() {
        Users user = new Users();
        user.setUserId(1);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("Recruiter"));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(recruiterProfileRepository.findById(1)).thenReturn(Optional.of(new RecruiterProfile(user)));

        SecurityContextHolder.setContext(securityContext);

        Object result = usersService.getCurrentUserProfile();

        assertNotNull(result);
        assertTrue(result instanceof JobSeekerProfile);
    }

    @Test
    void testGetCurrentUserProfile_AnonymousAuthentication() {
        AnonymousAuthenticationToken anonymousToken = mock(AnonymousAuthenticationToken.class);

        when(securityContext.getAuthentication()).thenReturn(anonymousToken);

        SecurityContextHolder.setContext(securityContext);

        Object result = usersService.getCurrentUserProfile();

        assertNull(result);
    }

    @Test
    void testGetCurrentUserProfile_UsernameNotFoundException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("nonexistent@example.com");

        when(usersRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        SecurityContextHolder.setContext(securityContext);

        assertThrows(UsernameNotFoundException.class, () -> usersService.getCurrentUserProfile());
    }



    @Test
    void testGetCurrentUserProfile_AuthenticatedRecruiter() {
        Users user = new Users();
        user.setUserId(1);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("Recruiter"));

        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn("test@example.com").when(authentication).getName();
        doReturn(authorities).when(authentication).getAuthorities();
        doReturn(Optional.of(user)).when(usersRepository).findByEmail("test@example.com");
        doReturn(Optional.of(new RecruiterProfile(user))).when(recruiterProfileRepository).findById(1);

        SecurityContextHolder.setContext(securityContext);

        Object result = usersService.getCurrentUserProfile();

        assertNotNull(result);
        assertTrue(result instanceof RecruiterProfile);
    }


    @Test
    void testGetCurrentUser() {
        Users user = new Users();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        SecurityContextHolder.setContext(securityContext);
        Users result = usersService.getCurrentUser();

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testGetCurrentUser_UserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("nonexistent@example.com");
        when(usersRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        SecurityContextHolder.setContext(securityContext);

        assertThrows(UsernameNotFoundException.class, () -> usersService.getCurrentUser());
    }

    @Test
    void testGetCurrentUser_AnonymousAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(mock(AnonymousAuthenticationToken.class));

        SecurityContextHolder.setContext(securityContext);

        Users result = usersService.getCurrentUser();

        assertNull(result);
    }



    @Test
    void testFindByEmail() {
        String email = "test@example.com";
        Users user = new Users();
        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Users result = usersService.findByEmail(email);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testFindByEmail_UserNotFound() {
        String email = "test@example.com";
        when(usersRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> usersService.findByEmail(email));
    }
}

