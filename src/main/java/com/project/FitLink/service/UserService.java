package com.project.FitLink.service;

import com.project.FitLink.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // this is copied from last proj and will be deleted

    public void removeAllUsers(){
        userRepository.deleteAll();
    }
}
