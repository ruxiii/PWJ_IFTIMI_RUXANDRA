# WORKLY

### **Business Requirements**

1. **User Registration:** The platform must allow users to register for a new account as either recruiters or job seekers.
2. **Login/Logout:** Users must be able to securely log in and log out of the system.
3. **Profile Creation and Management:** Job seekers and recruiters must be able to create and update detailed profiles, including personal and professional information, and upload profile photos.
4. **Job Posting:** Recruiters must be able to post jobs, including job descriptions, locations, and company details.
5. **Job Search:** Job seekers must have the ability to search for jobs based on various criteria such as job title, location, and employment type.
6. **Application Management:** Job seekers must be able to apply for jobs by submitting cover letters and resumes through the platform.
7. **Save Jobs:** Job seekers should be able to save jobs of interest.
8. **Skill Management:** Job seekers must have the ability to list skills with associated experience levels and years of experience.
9. **Uploading/Downloading resume:** Job seekers should be able to upload their resumes during profile creation or when editing their profile, and recruiters should be able to download these resumes for review.
10. **Authorization Levels:** The system should differentiate user types (recruiter, job seeker) and provide appropriate access and functionality based on their roles.


### **Features**

### **1. User Management**

The platform will allow users to create accounts, log in, and manage their profiles. This includes:

- **Recruiters:** Ability to register with recruiter priviledges.
- **Job Seekers:** Ability to register with basic personal and professional details.
- Account security features, including password protection.
- Registration and login/logout functionality.
- Differentiation between user types (recruiter and job seeker).

---

### **2. Profile Management**

Users will have dedicated profiles to manage their personal and professional information. This includes:

- **Recruiters:** Add company name, location, and profile photo.
- **Job Seekers:** Add resume, skills, work authorization status, and employment preferences.
- Editable profile sections (location, work authorization).
- Ability to upload documents (resumes).
- Skill management with experience levels.

---

### **3. Job Posting and Search**

Recruiters can post job openings, and job seekers can search for available jobs using filters such as job title, location, and employment type.

- **Job Posting:** Recruiters add job title, description, location, salary, and remote options.
- **Job Search:** Advanced search filters for job seekers.
- Job posting management.

---

### **4. Job Applications**

Job seekers can apply to job postings directly from the platform by submitting their resume and cover letter.

- Ability to apply for jobs with an uploaded or previously stored resume and cover letter.

---

### **5. Save and Track Jobs**

Job seekers can save jobs they are interested in.

- Save jobs for later review.
- View saved jobs in a dedicated section.

---

### Conclusion:

These 5 features form the foundation of the platform’s MVP phase, focusing on delivering core functionalities to connect recruiters and job seekers efficiently.



- Common requirements
    - Register for a new account
    - Login/Logout

- Requirements - Recruiter
    - Post a new job
    - View our jobs
    - View list of candidates that have applied for a job
    - Edit profile and upload profile photo 

- Requirements - Candidate
    - Search for jobs
    - Apply for a job
    - View list of jobs that the candidate has applied for
    - Edit profile and upload profile photo
    - Upload CV

- Relations between entities:
    - Recruiter profile : Users => 1:1 (a recruiter profile is associated with one user)
    - Users type : Users => 1:M (an UserType can have many users; many users can have one UserType)
    - Users : Job post activity => 1:M (one user can have applications for many jobs)
    - Users : Job seeker profile => 1:1 (a job seeker profile is associated with one user)
    - Job seeker profile : Job seeker apply => 1:M (a job seeker profile can apply to multiple jobs)
    - Job seeker profile : Job seeker save => 1:M (a job seeker profile can have many items saved)
    - Job seeker profile : Skills => 1:M (a job seeker can have one or many skills)
    - Job post activity : Job seeker apply => 1:M (a job post activity can have multiple people who've applied to it)
    - Job post activity : Job seeker save => 1:M (a job post activity can have many candidates saved)
    - Job post activity : Job location => M:1 (many jobs can have the same location)
    - Job post activity : Job company => M:1 (many jobs can be in the same company)
    ![alt text](image.png)

- Database entities:
    - Users: basic information about an user
    - UsersType: an user role
