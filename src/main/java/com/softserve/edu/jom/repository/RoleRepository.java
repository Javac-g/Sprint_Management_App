package com.softserve.edu.jom.repository;

import com.softserve.edu.jom.model.Role;
import com.softserve.edu.jom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByRole(User.Role role);
}
