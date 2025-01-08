package com.project.jobportal.controller;

import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.UsersRepository;
import com.project.jobportal.service.RecruiterProfileService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final UsersRepository usersRepository;

    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(UsersRepository usersRepository, RecruiterProfileService recruiterProfileService) {
        this.usersRepository = usersRepository;
        this.recruiterProfileService = recruiterProfileService;
    }

    @GetMapping("/")
    public String recruiterProfile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUserName = authentication.getName();
            Users users = usersRepository.findByEmail(currentUserName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(users.getUserId());

            if(!recruiterProfile.isEmpty()){
                model.addAttribute("profile", recruiterProfile.get());
            }
        }
        return "recruiter-profile";
    }

    @PostMapping("/addNew")
    public String addNew(RecruiterProfile recruiterProfile, @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        recruiterProfileService.addNew(recruiterProfile, multipartFile, model);
        return "redirect:/dashboard/";
    }
}
