package com.project.easysign.controller;

import com.project.easysign.dto.UserDTO;
import com.project.easysign.s3.S3Service;
import com.project.easysign.service.MailService;
import com.project.easysign.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final MailService mailService;
    private final S3Service s3Service;
    @PostMapping("/join")
    public ResponseEntity register(@RequestPart("img")MultipartFile multipartFile,
                                   @RequestPart("user") @Valid UserDTO.Request request){
        String imgUrl = null;
        if(multipartFile != null){
            imgUrl = s3Service.uploadImage(multipartFile);
        }
        String status = userService.register(request, imgUrl);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
    @GetMapping("/userId/{userId}/exists")
    public ResponseEntity checkIdDuplicate(@PathVariable String userId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.checkUserIdDuplicaton(userId));
    }
    @GetMapping("/email/{email}/exists")
    public ResponseEntity checkEmailDuplicate(@PathVariable String email){
        return ResponseEntity.status(HttpStatus.OK).body(userService.checkEmailDuplicaton(email));
    }
    // 이메일 인증
    @PostMapping("/mailConfirm")
    String mailConfirm(@RequestParam("email") String email) throws Exception {
        String code = mailService.sendSimpleMessage(email);
        return code;
    }

}
