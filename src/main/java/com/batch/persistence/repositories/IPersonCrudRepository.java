package com.batch.persistence.repositories;

import com.batch.persistence.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPersonCrudRepository extends JpaRepository<Person,Long> {
    Optional<Person> findPersonById(Long id);
    Optional<Person> findPersonByName(String name);
 }
