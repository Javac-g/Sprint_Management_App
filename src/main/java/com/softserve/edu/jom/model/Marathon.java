package com.softserve.edu.jom.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = {"users", "sprints"})
public class Marathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title must be populated")
    @Size(min = 2, max = 50, message =
        "Title must be between 2 and 50 characters")
    private String title;

    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "marathon_user",
        joinColumns = @JoinColumn(name = "marathon_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>(0);

    @OneToMany(mappedBy = "marathon")
    private Set<Sprint> sprints = new HashSet<>(0);
}
