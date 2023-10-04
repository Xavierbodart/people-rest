package com.box.people.rest.mapper;

import com.box.people.rest.model.person.PersonCO;
import com.box.people.rest.model.person.PersonEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonCO mapToCO(PersonEntity personEntity);

    @InheritInverseConfiguration
    PersonEntity mapFromCO(PersonCO personCO);
}