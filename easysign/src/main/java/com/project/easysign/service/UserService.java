package com.project.easysign.service;

import com.project.easysign.domain.Role;
import com.project.easysign.domain.User;
import com.project.easysign.dto.UserDTO;
import com.project.easysign.exception.AlreadyExistsException;
import com.project.easysign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public String register(UserDTO.Request request, String imgUrl) {
        Optional<User> optionalUser = userRepository.findByUserId(request.getUserId());
        if(optionalUser.isPresent()){
            throw new AlreadyExistsException("이미 존재하는 아이디입니다.");
        }
        String pass = passwordEncoder.encode(request.getPw());
        userRepository.save(request.toEntity(imgUrl, Role.USER, pass));
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
}
