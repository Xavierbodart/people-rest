package com.box.people.rest.controller;


import com.box.people.rest.model.PersonCO;
import com.box.people.rest.model.base.ResultObject;
import com.box.people.rest.service.PersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Api(tags = {"People"})
@RestController
@RequestMapping("/people")
public class PeopleController extends AbstractController {

    private PersonService personService;

    public PeopleController(final PersonService personService) {
        super("People management");
        this.personService = personService;
    }

    @ApiOperation("Create person")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultObject<PersonCO> createPerson(@Valid @RequestBody PersonCO person) {
        return mapToResultObject(personService.savePerson(person));
    }

    @ApiOperation("Update person")
    @PutMapping("/{id}")
    public ResultObject<PersonCO> updatePerson(@PathVariable("id") UUID id, @Valid @RequestBody PersonCO person) {
        return mapToResultObject(personService.updatePerson(id, person));
    }

    @ApiOperation("Delete person")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResultObject<?> deletePerson(@PathVariable("id") UUID id) {
        personService.deletePerson(id);
        return mapToResultObject(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("Get person")
    @GetMapping("/{id}")
    public ResultObject<PersonCO> getPerson(@PathVariable("id") UUID id) {
        return mapToResultObject(personService.getPerson(id));
    }

    @ApiOperation("List people")
    @GetMapping()
    public ResultObject<List<PersonCO>> listPeople(@RequestParam(value = "firstName", required = false) String firstName,
                                                   @RequestParam(value = "lastName", required = false) String lastName) {
        return mapToResultObject(personService.filterPeople(firstName, lastName));
    }
}
