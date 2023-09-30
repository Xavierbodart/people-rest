package com.box.people.rest.service;

import com.box.people.rest.dao.PersonRepository;
import com.box.people.rest.mapper.PersonMapper;
import com.box.people.rest.model.PersonCO;
import com.box.people.rest.model.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Autowired
    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public PersonCO savePerson(final PersonCO personCO) {
        personCO.setId(UUID.randomUUID());
        PersonEntity personEntity = personMapper.mapFromCO(personCO);
        return personMapper.mapToCO(personRepository.save(personEntity));
    }

    public PersonCO updatePerson(final UUID id, final PersonCO personCO) {
        if (!personRepository.existsById(id.toString())) {
            throw new NoSuchElementException("no person with id " + id.toString());
        }
        PersonEntity personEntity = personMapper.mapFromCO(personCO);
        personEntity.setId(id.toString());
        return personMapper.mapToCO(personRepository.save(personEntity));
    }

    public PersonCO getPerson(final UUID id) {
        return personMapper.mapToCO(personRepository.getOne(id.toString()));
    }

    public void deletePerson(final UUID id) {
        personRepository.deleteById(id.toString());
    }

    public List<PersonCO> filterPeople(final String firstName, final String lastName) {
        final List<PersonEntity> personEntityEntities = personRepository.filterPeopleByFirstNameAndLastName(firstName,
                lastName);
        return personEntityEntities.stream().map(personMapper::mapToCO).collect(Collectors.toList());
    }
}
