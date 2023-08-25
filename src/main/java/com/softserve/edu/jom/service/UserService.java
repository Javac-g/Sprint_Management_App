package com.softserve.edu.jom.service;

import com.softserve.edu.jom.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getUserById(Long id);

    User getStudentById(Long id);

    User createOrUpdateUser(User user);

    List<User> getAllByRole(String role);

    List<User> getAllByRoleAndMarathonId(String role, Long marathonId);

    boolean addUserToMarathon(User user, Long marathonId);

    boolean removeUserFromMarathon(Long userId, Long marathonId);
}
