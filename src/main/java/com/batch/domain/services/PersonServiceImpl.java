package com.batch.domain.services;

import com.batch.domain.dto.PersonDto;
import com.batch.persistence.repositories.IPersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PersonServiceImpl implements IPersonService{
    private final IPersonRepository personRepository;
    @Override
    @Transactional(readOnly = true)
    public List<PersonDto> getAll() {
        return this.personRepository.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonDto> getPersonById(Long id) {
        Optional<PersonDto> personOpt = this.personRepository.getPersonById(id);
        if (personOpt.isEmpty()){
            throw new RuntimeException("User no found");
        }
        return personOpt;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonDto> getPersonByName(String name) {
        Optional<PersonDto> personOpt = this.personRepository.getPersonByName(name);
        if (personOpt.isEmpty()){
            throw new RuntimeException("User no found");
        }
        return personOpt;
    }

    @Override
    @Transactional
    public Optional<PersonDto> save(PersonDto person) {
        return this.personRepository.save(person);
    }

    @Override
    @Transactional
    public Optional<PersonDto> update(PersonDto person) {
        Optional<PersonDto> personOpt = this.personRepository.getPersonById(person.getId());
        if (personOpt.isEmpty()){
            throw new RuntimeException("User no found");
        }
        return this.personRepository.save(person);
    }

    @Override
    @Transactional
    public void saveAll(List<PersonDto> personDtoList) {
        this.personRepository.saveAll(personDtoList);
    }

    @Override
    @Transactional
    public Boolean delete(Long id) {
        Optional<PersonDto> personOpt = this.personRepository.getPersonById(id);
        if (personOpt.isEmpty()){
            throw new RuntimeException("User no found");
        }
        this.personRepository.delete(id);
        return true;
    }
}
