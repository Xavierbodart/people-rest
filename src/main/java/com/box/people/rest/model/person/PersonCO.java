package com.box.people.rest.model.person;


import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

@ApiModel(value = "Person")
public class PersonCO {
    private UUID id;
    @NotEmpty(message = "firstName is required")
    @Size(max = 50, message = "firstName must be less than 50 characters")
    private String firstName;
    @NotEmpty(message = "lastName is required")
    @Size(max = 50, message = "lastName must be less than 50 characters")
    private String lastName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
