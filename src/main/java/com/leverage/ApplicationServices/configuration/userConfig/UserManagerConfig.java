package com.leverage.ApplicationServices.configuration.userConfig;

import com.leverage.ApplicationServices.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserManagerConfig implements UserDetailsService {

    private final UserRepo userRepo;

    public UserManagerConfig(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo
                .findByMail(username)
                .map(UserInfoConfig::new)
                .orElseThrow(()-> new UsernameNotFoundException("User with emailId: "+username+" not found"));
    }
}
