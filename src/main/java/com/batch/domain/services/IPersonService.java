package com.batch.domain.services;

import com.batch.domain.dto.PersonDto;

import java.util.List;
import java.util.Optional;

public interface IPersonService {
    List<PersonDto> getAll();
    Optional<PersonDto> getPersonById(Long id);
    Optional<PersonDto> getPersonByName(String name);
    Optional<PersonDto> save(PersonDto person);
    Optional<PersonDto> update(PersonDto person);
    void saveAll(List<PersonDto> personDtoList);
    Boolean delete(Long id);
}
