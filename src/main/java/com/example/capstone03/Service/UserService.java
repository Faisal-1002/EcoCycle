package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.ContainerRequest;
import com.example.capstone03.Model.User;
import com.example.capstone03.Repository.ContainerRequestRepository;
import com.example.capstone03.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ContainerRequestRepository containerRequestRepository;
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        user.setPoints(0.0);
        userRepository.save(user);
    }

    public void updateUser(Integer id, User updatedUser) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found");
        }

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setAddress(updatedUser.getAddress());
        user.setPhone_number(updatedUser.getPhone_number());
        user.setPoints(updatedUser.getPoints());

        userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found");
        }

        userRepository.delete(user);
    }

    // 1. Get user by ID
    public User getUserById(Integer id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found");
        }

        return user;
    }

    // 2. Get all container requests by user ID
    public List<ContainerRequest> getRequestsByUserId(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) throw new ApiException("User not found");
        return containerRequestRepository.findAllByUser(user);
    }

}
