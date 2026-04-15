package com.leandrosilveira.jobradar.repository;

import com.leandrosilveira.jobradar.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByLocationIgnoreCase(String Location);

    List<Job> findByTitleContainingIgnoreCase(String keyword);

    Optional<Job> findByUrl(String url);
}
