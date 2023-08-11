package com.project.easysign.security;

import com.project.easysign.domain.User;
import com.project.easysign.exception.NonExistentUserException;
import com.project.easysign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username)
                .orElseThrow(()-> new NonExistentUserException());
        return new UserPrincipal(user);
    }
}
