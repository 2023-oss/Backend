package com.project.easysign.service;

import com.project.easysign.domain.Form;
import com.project.easysign.domain.Template;
import com.project.easysign.dto.FormDTO;
import com.project.easysign.dto.PageResponse;
import com.project.easysign.exception.NonExistentTemplateException;
import com.project.easysign.repository.FormRepository;
import com.project.easysign.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class FormService {
    private final FormRepository formRepository;
    private final TemplateRepository templateRepository;
    public String makeForm(Long templateId, String imgUrl, FormDTO.Request formDTO, String jsonData) {
        Template template = templateRepository.findById(templateId)
                        .orElseThrow(()-> new NonExistentTemplateException());
        formRepository.save(Form.builder()
                .template(template)
                .name(formDTO.getName())
                .phone(formDTO.getPhone())
                .sign(imgUrl)
                .jsonData(jsonData)
                .build());
        return "SUCCESS";
    }

    public PageResponse getList(Pageable pageable) {
        Page<Form> formPage = formRepository.findAll(pageable);
        List<FormDTO.Response> formList = formPage.map(FormDTO.Response::new)
                .stream().collect(Collectors.toList());

        return PageResponse.builder()
                .forms(formList)	// todoDtoPage.getContent()
                .pageNo(pageable.getPageNumber()+1)
                .pageSize(pageable.getPageSize())
                .totalElements(formPage.getTotalElements())
                .totalPages(formPage.getTotalPages())
                .last(formPage.isLast())
                .build();
    }
}
