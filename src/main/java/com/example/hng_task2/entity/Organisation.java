package com.example.hng_task2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "orgId"))
public class Organisation {

    @Id
    @Column(name = "orgId", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orgId;

    @NotNull
    private String name;

    private String description;

    @ManyToMany
    private List<User> users;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    public Organisation(
            String name,
            String description
    ) {
        this.orgId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
    }
}
