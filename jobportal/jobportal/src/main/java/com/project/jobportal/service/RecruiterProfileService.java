package com.project.jobportal.service;

import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.RecruiterProfileRepository;
import com.project.jobportal.repository.UsersRepository;
import com.project.jobportal.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;

    private final UsersRepository usersRepository;

    @Autowired
    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UsersRepository usersRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<RecruiterProfile> getOne(Integer id){
        return recruiterProfileRepository.findById(id);
    }

    public void addNew(RecruiterProfile recruiterProfile, MultipartFile multipartFile, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUserName = authentication.getName();
            Users users = usersRepository.findByEmail(currentUserName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            recruiterProfile.setUserId(users);
            recruiterProfile.setUserAccountId(users.getUserId());
        }

        model.addAttribute("profile", recruiterProfile);
        String fileName = "";
        if (!multipartFile.getOriginalFilename().equals("")) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(fileName);
        }
        RecruiterProfile savedUser = recruiterProfileRepository.save(recruiterProfile);

        String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();
        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public RecruiterProfile getCurrentRecruiterProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(()
                    -> new UsernameNotFoundException("User not found"));
            Optional<RecruiterProfile> recruiterProfile = getOne(users.getUserId());
            return recruiterProfile.orElse(null);
        } else {
            return null;
        }
    }
}
