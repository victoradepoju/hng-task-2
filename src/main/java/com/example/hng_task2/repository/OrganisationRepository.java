package com.example.hng_task2.repository;

import com.example.hng_task2.entity.Organisation;
import com.example.hng_task2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganisationRepository extends JpaRepository<Organisation, String> {
    List<Organisation> findByCreator(User creator);
//    Optional<Organisation> save(Organisation organisation);
}