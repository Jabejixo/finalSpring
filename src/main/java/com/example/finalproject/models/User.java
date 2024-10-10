package com.example.finalproject.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "authentication_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    @Email(message = "Email should be valid")
    private String email;

    @Column(nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Column(nullable = false)
    @Size(min = 2, max = 25)
    private String firstName;

    @Column(nullable = false)
    @Size(min = 2, max = 25)
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "role_id")
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();

}

