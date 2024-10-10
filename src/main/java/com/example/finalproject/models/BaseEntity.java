package com.example.finalproject.models;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseEntity {
    @Id
    private UUID id;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID();
    }
}
