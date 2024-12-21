package com.project.jobportal.repository;

import com.project.jobportal.entity.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsRepositrory extends JpaRepository<Skills, Integer> {
}
