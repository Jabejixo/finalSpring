package com.example.finalproject.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"tasks", "users"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Project extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Task> tasks;

    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ", description='" + description + '\'' +
                '}';
    }

}
