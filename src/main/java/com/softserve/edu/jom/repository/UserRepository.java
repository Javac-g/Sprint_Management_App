package com.softserve.edu.jom.repository;

import com.softserve.edu.jom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select user from User user where user.role.role =:role")
    List<User> getAllByRole(@Param("role") User.Role role);

    User getUserById(Long id);

    User getUserByEmail(String email);

    @Query("select user from User user where user.id =:userId and user.role.role =:role")
    User getUserByIdAndRole(@Param("userId") Long userId, @Param("role") User.Role role);

    @Query("select user from User user join user.marathons maraphon where user.role.role =:role and maraphon.id =:marathonId")
    List<User> findByRoleAndMarathonId(@Param("role") User.Role role, @Param("marathonId") Long marathonId);
}
