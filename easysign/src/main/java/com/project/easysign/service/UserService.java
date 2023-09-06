package com.project.easysign.service;

import com.project.easysign.domain.Role;
import com.project.easysign.domain.Template;
import com.project.easysign.domain.User;
import com.project.easysign.dto.UserDTO;
import com.project.easysign.exception.AlreadyExistsException;
import com.project.easysign.exception.NonExistentException;
import com.project.easysign.exception.NonExistentUserException;
import com.project.easysign.repository.TemplateRepository;
import com.project.easysign.repository.UserRepository;
import com.project.easysign.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final PasswordEncoder passwordEncoder;
    public String register(UserDTO.Request request, String imgUrl) {
        Optional<User> optionalUser = userRepository.findByUserId(request.getUserId());
        if(optionalUser.isPresent()){
            throw new AlreadyExistsException("이미 존재하는 아이디입니다.");
        }
        String pass = passwordEncoder.encode(request.getPw());
        User user = userRepository.save(request.toEntity(imgUrl, Role.USER, pass));
        templateRepository.save(Template.builder()
                .templateId(UUID.randomUUID().toString())
                .user(user)
                .build());

        return "SUCCESS";
    }
    @Transactional(readOnly = true)
    public String checkUserIdDuplicaton(String userId){
        boolean useridDuplication = userRepository.existsByUserId(userId);
        if(useridDuplication){
            throw new AlreadyExistsException("이미 존재하는 아이디입니다.");
        }
        return "SUCCESS";
    }
    @Transactional(readOnly = true)
    public String checkEmailDuplicaton(String email){
        boolean emailDuplication = userRepository.existsByEmail(email);
        if(emailDuplication){
            throw new AlreadyExistsException("이미 존재하는 이메일입니다.");
        }
        return "SUCCESS";
    }

    public Long findUserTeplateId(UserPrincipal loginUser) {
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(()-> new NonExistentUserException());
        Template template = templateRepository.findByUser(user)
                .orElseThrow(()-> new NonExistentException("존재하지 않는 동의서 양식 입니다."));
        return template.getId();
    }
}
