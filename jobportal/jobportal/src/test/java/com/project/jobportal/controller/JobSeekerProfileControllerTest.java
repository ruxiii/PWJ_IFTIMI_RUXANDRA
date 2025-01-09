package com.project.jobportal.controller;

import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.repository.UsersRepository;
import com.project.jobportal.service.JobSeekerProfileService;
import com.project.jobportal.util.FileDownloadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class JobSeekerProfileControllerTest {

    @Mock
    private JobSeekerProfileService jobSeekerProfileService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private Model model;

    @Mock
    private MultipartFile image;

    @Mock
    private MultipartFile pdf;

    @Mock
    private FileDownloadUtil fileDownloadUtil;

    @Mock
    private Resource resource;

    @InjectMocks
    private JobSeekerProfileController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testJobSeekerProfile() {
        String viewName = controller.jobSeekerProfile(model);

        verify(jobSeekerProfileService, times(1)).jobSeekerProfile(model);
        assertEquals("job-seeker-profile", viewName);
    }

    @Test
    void testAddNew() {
        JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();

        String viewName = controller.addNew(jobSeekerProfile, image, pdf, model);

        verify(jobSeekerProfileService, times(1)).addNew(eq(jobSeekerProfile), eq(image), eq(pdf), eq(model));
        assertEquals("redirect:/dashboard/", viewName);
    }

    @Test
    void testCandidateProfile() {
        int id = 1;
        JobSeekerProfile profile = new JobSeekerProfile();
        when(jobSeekerProfileService.getOne(id)).thenReturn(Optional.of(profile));

        String viewName = controller.candidateProfile(id, model);

        verify(jobSeekerProfileService, times(1)).getOne(id);
        verify(model, times(1)).addAttribute("profile", profile);
        assertEquals("job-seeker-profile", viewName);
    }

//    @Test
//    void testSuccessfulFileDownload() throws Exception {
//        Resource mockResource = new ByteArrayResource("dummy content".getBytes());
//        Mockito.when(fileDownloadUtil.getFileAsResourse(Mockito.anyString(), Mockito.anyString()))
//                .thenReturn(mockResource);
//
//        mockMvc.perform(get("/downloadResume")
//                        .param("fileName", "resume.pdf")
//                        .param("userID", "123"))
//                .andExpect(status().isOk())
//                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\""))
//                .andExpect(content().bytes("dummy content".getBytes()));
//    }
//
//
//    @Test
//    void testFileNotFound() throws Exception {
//        Mockito.when(fileDownloadUtil.getFileAsResourse(Mockito.anyString(), Mockito.anyString()))
//                .thenReturn(null);
//
//        mockMvc.perform(get("/downloadResume")
//                        .param("fileName", "resume.pdf")
//                        .param("userID", "123"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("File not found"));
//    }
//
//    @Test
//    void testUserDirectoryNotFound() throws Exception {
//        Mockito.when(fileDownloadUtil.getFileAsResourse(Mockito.anyString(), Mockito.anyString()))
//                .thenReturn(null);
//
//        mockMvc.perform(get("/downloadResume")
//                        .param("fileName", "resume.pdf")
//                        .param("userID", "nonexistentUser"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("File not found"));
//    }


}


