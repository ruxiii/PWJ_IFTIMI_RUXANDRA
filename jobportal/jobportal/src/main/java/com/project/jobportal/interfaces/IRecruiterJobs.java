package com.project.jobportal.interfaces;

public interface IRecruiterJobs {
    Long getTotalCandidates();

    Integer getJob_post_id();

    String getJob_title();

    int getLocationId();

    String getCity();

    String getState();

    String getCountry();

    int getCompanyId();

    String getName();
}
