package com.victor.hng_task2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "email"}))
public class User implements UserDetails {

    @Id
    @Column(name = "userId", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private String phone;

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private List<Organisation> organisations;

    @OneToMany(mappedBy = "creator")
    private List<Organisation> createdOrganisations;

    // Constructor to generate UUID
    public User(
            String firstName,
            String lastName,
            String email,
            String password,
            String phone
    ) {
        this.userId = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
