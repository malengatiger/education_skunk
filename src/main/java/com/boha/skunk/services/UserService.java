package com.boha.skunk.services;

import com.boha.skunk.data.User;
import com.boha.skunk.data.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {

        return null;
    }

    public List<User> getOrganizationUsers(Long organizationId) {

        return null;
    }
}
