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


//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepo
//                .findByMail(username)
//                .map(UserInfoConfig::new)
//                .orElseThrow(()-> new UsernameNotFoundException("User with emailId: "+username+" not found"));

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            System.out.println("🔍 Loading user from DB for email: " + username);

            return userRepo
                    .findByMail(username)
                    .map(user -> {
                        System.out.println(" User found. Password (from DB): " + user.getPassword());
                        return new UserInfoConfig(user);
                    })
                    .orElseThrow(() -> {
                        System.out.println(" User not found for email: " + username);
                        return new UsernameNotFoundException("User with emailId: " + username + " not found");
                    });
        }

    }

