package com.softserve.edu.jom.service;

import com.softserve.edu.jom.exception.DuplicateUserEmailException;
import com.softserve.edu.jom.model.Marathon;
import com.softserve.edu.jom.model.User;
import com.softserve.edu.jom.repository.MarathonRepository;
import com.softserve.edu.jom.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@PreAuthorize("hasAuthority('MENTOR')")
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private MarathonRepository marathonRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        Collections.sort(users, Comparator.comparing(user -> user.getId()));
        return users;
    }

    @Override
    public User getUserById(Long id) {
        Validate.notNull(id, "User id must not be null!");
        return userRepository.getUserById(id);
    }

    @Override
    public User getStudentById(Long id) {
        Validate.notNull(id, "User id must not be null!");
        return userRepository.getUserByIdAndRole(id, User.Role.TRAINEE);
    }

    @Override
    public User createOrUpdateUser(User user) {
        try {
            if (user.getId() != null) {
                User existedUser = getUserById(user.getId());
                existedUser.setFirstName(user.getFirstName());
                existedUser.setLastName(user.getLastName());
                existedUser.setEmail(user.getEmail());
                existedUser.setRole(user.getRole());
                return userRepository.save(existedUser);
            }
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate user email exception raised when trying to save user with email %s", user.getEmail());
            throw new DuplicateUserEmailException(String.format("Duplicate user email %s", user.getEmail()), e);
        }

    }

    @Override
    public List<User> getAllByRole(String role) {
        return userRepository.getAllByRole(User.Role.valueOf(role.toUpperCase()));

    }

    @Override
    public List<User> getAllByRoleAndMarathonId(String role, Long marathonId) {
        return userRepository.findByRoleAndMarathonId(User.Role.valueOf(role.toUpperCase()), marathonId);

    }

    @Override
    public boolean addUserToMarathon(User user, Long marathonId) {
        try {
            Marathon existedMarathon = marathonRepository.getOne(marathonId);
            Validate.notNull(existedMarathon, "Marathon with id = %s is not found!", marathonId);
            existedMarathon.getUsers().add(user);
            return marathonRepository.save(existedMarathon) != null;
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate user email exception raised when trying to save user with email %s", user.getEmail());
            throw new DuplicateUserEmailException(String.format("Duplicate user email %s", user.getEmail()), e);
        }
    }

    @Override
    public boolean removeUserFromMarathon(Long userId, Long marathonId) {
        User user = getUserById(userId);
        Validate.notNull(user, "User with id = %s is not found!", userId);
        Marathon marathon = marathonRepository.getOne(marathonId);
        Validate.notNull(marathon, "Marathon with id = %s is not found!", marathonId);
        Set<User> newUserLinkedSet = marathon.getUsers().stream().filter(u -> u.getId() != userId).collect(Collectors.toSet());
        marathon.getUsers().clear();
        marathon.getUsers().addAll(newUserLinkedSet);
        marathonRepository.save(marathon);
        return true;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setMarathonRepository(MarathonRepository marathonRepository) {
        this.marathonRepository = marathonRepository;
    }

    @Autowired
    public void setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
}
