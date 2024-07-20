package com.batch.persistence.repositories;

import com.batch.domain.dto.PersonDto;
import com.batch.persistence.entities.Person;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class PersonRepositoryImpl implements IPersonRepository {
    private final IPersonCrudRepository crudRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PersonDto> getAll() {
        return this.crudRepository.findAll()
                .stream()
                .map(person -> this.modelMapper.map(person, PersonDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PersonDto> getPersonById(Long id) {
        return this.crudRepository.findPersonById(id)
                .map(person -> this.modelMapper.map(person, PersonDto.class));
    }

    @Override
    public Optional<PersonDto> getPersonByName(String name) {
        return this.crudRepository.findPersonByName(name)
                .map(person -> this.modelMapper.map(person, PersonDto.class));
    }

    @Override
    public Optional<PersonDto> save(PersonDto person) {
        Optional<Person> savedPersonOpt = Optional.of(this.crudRepository.save(this.modelMapper.map(person, Person.class)));
        return savedPersonOpt.map(p -> this.modelMapper.map(p, PersonDto.class));
    }

    @Override
    public void saveAll(List<PersonDto> personDtoList) {
        List<Person> personList = personDtoList
                .stream()
                .map(personDto -> this.modelMapper.map(personDto, Person.class))
                .toList();
        this.crudRepository.saveAll(personList);
    }

    @Override
    public void delete(Long id) {
        this.crudRepository.deleteById(id);
    }
}
