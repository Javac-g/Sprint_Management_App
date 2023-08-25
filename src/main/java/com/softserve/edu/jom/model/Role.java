package com.softserve.edu.jom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Role must be populated")
    @Enumerated(EnumType.STRING)
    private User.Role role;

    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>(0);
}
