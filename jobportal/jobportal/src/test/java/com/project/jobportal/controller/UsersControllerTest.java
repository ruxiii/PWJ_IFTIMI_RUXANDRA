package com.project.jobportal.controller;

import com.project.jobportal.entity.Users;
import com.project.jobportal.entity.UsersType;
import com.project.jobportal.service.UsersService;
import com.project.jobportal.service.UsersTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsersControllerTest {

    @Mock
    private UsersTypeService usersTypeService;

    @Mock
    private UsersService usersService;

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsersController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testRegister() {
        List<UsersType> usersTypes = new ArrayList<>();
        when(usersTypeService.getAll()).thenReturn(usersTypes);

        String viewName = controller.register(model);

        verify(usersTypeService, times(1)).getAll();
        verify(model, times(1)).addAttribute("getAllTypes", usersTypes);
        verify(model, times(1)).addAttribute(eq("user"), any(Users.class));
        assertEquals("register", viewName);
    }

    @Test
    void testUserRegistration_UserExists() {
        Users user = new Users();
        user.setEmail("test@example.com");

        when(usersService.getUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        String viewName = controller.userRegistration(user, model);

        verify(usersService, times(1)).getUserByEmail(user.getEmail());
        verify(model, times(1)).addAttribute("error", "User with this email already exists");
        verify(model, times(1)).addAttribute(eq("getAllTypes"), any());
        verify(model, times(1)).addAttribute(eq("user"), any(Users.class));
        assertEquals("register", viewName);
    }

    @Test
    void testUserRegistration_NewUser() {
        Users user = new Users();
        user.setEmail("newuser@example.com");

        when(usersService.getUserByEmail(user.getEmail())).thenReturn(Optional.empty());

        String viewName = controller.userRegistration(user, model);

        verify(usersService, times(1)).getUserByEmail(user.getEmail());
        verify(usersService, times(1)).addNew(user);
        assertEquals("redirect:/dashboard/", viewName);
    }

    @Test
    void testLogin() {
        String viewName = controller.login();
        assertEquals("login", viewName);
    }

    @Test
    void testLogout() {
        // Mock dependencies
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextLogoutHandler logoutHandler = Mockito.spy(new SecurityContextLogoutHandler());

        // Mock SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);

        // Set up behavior for the mocks
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Call the logout method
        String viewName = controller.logout(request, response);

        // Verify interactions
        verify(securityContext, times(1)).getAuthentication();
        verify(logoutHandler, times(0)).logout(request, response, authentication); // No direct instance mocking required here.
        verify(authentication, never()).isAuthenticated();

        // Assert result
        assertEquals("redirect:/", viewName);
    }


}
