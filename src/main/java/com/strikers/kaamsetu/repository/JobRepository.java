package com.strikers.kaamsetu.repository;

import com.strikers.kaamsetu.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findBySkillRequiredIgnoreCaseAndActiveTrue(String skillRequired);
    List<Job> findByActiveTrue();
}
