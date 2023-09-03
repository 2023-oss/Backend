package com.project.easysign.repository;

import com.project.easysign.domain.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FormRepository extends JpaRepository<Form, Long> {

    Optional<Form> findByVpId(String vpId);
}
