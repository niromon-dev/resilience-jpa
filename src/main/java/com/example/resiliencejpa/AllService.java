package com.example.resiliencejpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllService {

    private final UserRepository userRepository;

    public List<UserEntity> all() {
        return userRepository.findAll();
    }

    public UserEntity create(UserEntity user) {
        return userRepository.save(user);
    }

    public List<UserEntity> allError() {
        throw new RuntimeException();
    }

    public int sleep(int delay) {
        return userRepository.sleep(delay);
    }

    public int sqlError() {
        return userRepository.sqlError();
    }
}
