package com.stockinsight.repository;

import com.stockinsight.model.entity.JobConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobConfigRepository extends JpaRepository<JobConfig, Long> {
    List<JobConfig> findByEnabledTrue();
}
