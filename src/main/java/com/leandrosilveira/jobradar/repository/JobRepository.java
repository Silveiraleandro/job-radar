package com.leandrosilveira.jobradar.repository;

import com.leandrosilveira.jobradar.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
