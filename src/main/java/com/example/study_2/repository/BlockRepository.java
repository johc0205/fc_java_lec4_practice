package com.example.study_2.repository;

import com.example.study_2.domain.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BlockRepository extends JpaRepository<Block, Long> {
}
