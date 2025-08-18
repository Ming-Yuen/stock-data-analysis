package com.stockinsight.repository;

import com.stockinsight.model.entity.BatchJonConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJonConfigRepository extends JpaRepository<BatchJonConfig, Long> {

}