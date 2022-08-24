package org.codej.instagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codej.instagram.model.Users;
import org.codej.instagram.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername={}",username);
        Users user = userRepository.findByUsername(username);

        CustomUserDetails userDetails = null;

        if(user != null){
            userDetails = new CustomUserDetails();
            userDetails.setUser(user);
        }else {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다."+username);
        }

        return userDetails;

    }
}
