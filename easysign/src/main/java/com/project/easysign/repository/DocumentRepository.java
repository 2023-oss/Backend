package com.project.easysign.repository;

import com.project.easysign.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("select distinct d from Document d " +
            "left join fetch d.blocks b " +
            "where d.id = :documentId ")
    Document findDocumentWithBlocks(@Param("documentId") Long documentId);

    @Query("select distinct d from Document d " +
            "left join fetch d.answers b " +
            "where d.id = :documentId ")
    Document findDocumentWithAnswers(Long documentId);
}
