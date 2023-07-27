package com.project.easysign.repository;

import com.project.easysign.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT DISTINCT d FROM Document d " + // DISTINCT 키워드를 사용해 주복된 결과 제거
            "LEFT JOIN FETCH d.categories c " +
            "WHERE d.id = :documentId")
    Document findDocumentWithCategories(@Param("documentId") Long documentId);
    @Query("SELECT DISTINCT d FROM Document d " + // DISTINCT 키워드를 사용해 중복된 결과 제거
            "LEFT JOIN FETCH d.categories c " +
            "LEFT JOIN FETCH c.blocks b " +
            "WHERE d.id = :documentId")
    Document findDocumentWithCategoriesAndBlocks(@Param("documentId") Long documentId);
}
