package com.project.easysign.repository;

import com.project.easysign.domain.Form;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FormRepository extends JpaRepository<Form, Long> {

    Optional<Form> findByVpId(String vpId);

    @Query(" select f from Form f " +
            "where f.template.id = :templateId and f.vpId = :vpId ")
    Page<Form> findAllByTemplateIdAndVpId(Pageable pageable, Long templateId, String vpId);
    @Query(" select f from Form f " +
            "where f.template.id = :templateId")
    Page<Form> findAllByTemplateId(Pageable pageable, Long templateId);




}
