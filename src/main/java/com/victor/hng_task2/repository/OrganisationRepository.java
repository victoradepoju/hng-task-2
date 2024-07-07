package com.victor.hng_task2.repository;

import com.victor.hng_task2.entity.Organisation;
import com.victor.hng_task2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganisationRepository extends JpaRepository<Organisation, String> {
    List<Organisation> findByCreator(User creator);
//    Optional<Organisation> save(Organisation organisation);
}
