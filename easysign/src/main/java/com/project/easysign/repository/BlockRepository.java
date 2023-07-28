package com.project.easysign.repository;

import com.project.easysign.domain.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    @Query("select distinct category from Block b ")
    List<String> findCategories(@Param("documentId") Long documentId);
}
