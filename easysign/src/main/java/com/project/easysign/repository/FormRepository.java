package com.project.easysign.repository;

import com.project.easysign.domain.Form;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FormRepository extends JpaRepository<Form, Long> {

}
