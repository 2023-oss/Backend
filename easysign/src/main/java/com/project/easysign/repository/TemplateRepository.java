package com.project.easysign.repository;

import com.project.easysign.domain.Template;
import com.project.easysign.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByUser(User user);

    Optional<Template> findByTemplateId(String templateId);
}
