package com.batch.persistence.repositories;

import com.batch.domain.dto.PersonDto;

import java.util.List;
import java.util.Optional;

public interface IPersonRepository {
    List<PersonDto> getAll();
    Optional<PersonDto> getPersonById(Long id);
    Optional<PersonDto> getPersonByName(String name);
    Optional<PersonDto> save(PersonDto person);
    void saveAll(List<PersonDto> personDtoList);
    void delete(Long id);
}
