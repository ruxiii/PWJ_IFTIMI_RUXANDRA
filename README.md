# WORKLY

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
    - Recruiter profile : Users => 1:1
    - Users type : Users => 1:M
    - Users : Job post activity => 1:M
    - Users : Job seeker profile => 1:M
    - Job seeker profile : Job seeker apply => 1:M
    - Job seeker profile : Job seeker save => 1:M
    - Job seeker profile : Skills => 1:M
    - Job post activity : Job seeker apply => 1:M
    - Job post activity : Job seeker save => 1:M
    - Job post activity : Job location => M:1
    - Job post activity : Job company => M:1
    ![alt text](image.png)