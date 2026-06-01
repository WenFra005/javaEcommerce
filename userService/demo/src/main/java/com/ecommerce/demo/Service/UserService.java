package com.ecommerce.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.demo.Model.User;
import com.ecommerce.demo.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setName(updatedUser.getName());
            user.setUserEmail(updatedUser.getUserEmail());
            user.setUserPassword(updatedUser.getUserPassword());
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateStatus(Long id, String status) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setUserStatus(null);
            userRepository.save(user);
        }
    }


}
