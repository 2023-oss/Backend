package com.project.easysign.controller;

import com.project.easysign.domain.User;
import com.project.easysign.dto.FormDTO;
import com.project.easysign.dto.PageResponse;
import com.project.easysign.exception.AuthenticationFailedException;
import com.project.easysign.s3.S3Service;
import com.project.easysign.security.UserPrincipal;
import com.project.easysign.service.FormService;
import com.project.easysign.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/form")
public class FormController {
    private final FormService formService;
    private final S3Service s3Service;

    private final UserService userService;

    @PostMapping("/register/{templateId}")
    public ResponseEntity registerForm(@PathVariable("templateId") String templateId, @RequestBody String jsonData) {
        return ResponseEntity.status(HttpStatus.OK).body(formService.registerForm(templateId, jsonData));
    }

//    @GetMapping("/viewAll/{templateId}")
//    public ResponseEntity viewAll(@PathVariable("templateId") Long templateId
    @GetMapping("/viewAll")
    public ResponseEntity viewAll(@AuthenticationPrincipal UserPrincipal loginUser,
                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5)Pageable pageable,
                                  @RequestParam(value="search", required = false) String search){
        if(loginUser==null){
            throw new AuthenticationFailedException("로그인 후 사용해주세요.");
        }
        Long templateId = userService.findUserTeplateId(loginUser);

        if(search==null){
            return ResponseEntity.status(HttpStatus.OK).body(formService.getList(pageable, templateId));
        }
        if(search.equals("")){
            return ResponseEntity.status(HttpStatus.OK).body(formService.getList(pageable, templateId));
        }
        return ResponseEntity.status(HttpStatus.OK).body(formService.getList(pageable,templateId, search));
    }

    @GetMapping("/select/{vp_id}")
    public ResponseEntity select(@PathVariable("vp_id") String vp_id){
        return ResponseEntity.status(HttpStatus.OK).body(formService.select(vp_id));
    }


}
