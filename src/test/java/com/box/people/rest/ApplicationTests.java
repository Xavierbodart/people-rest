package com.box.people.rest;

import com.box.people.rest.model.base.ResultObject;
import com.box.people.rest.model.person.PersonCO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = BoxPeopleRestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {
    public static final String RESOURCE_NAME = "people";


    private static final String FIRST_NAME = "Test-Firstname";
    private static final String LAST_NAME = "Test-Lastname";
    private static final String FIRST_NAME_UPDATED = "Test-Firstname Updated";
    private static final String LAST_NAME_UPDATED = "Test-Lastname Updated";

    private static UUID id;
    private static String getRootUrl() {
        return "http://localhost:8080";
    }
    private static String getResourceUrl() {
        return getRootUrl() + "/" + RESOURCE_NAME;
    }

    private static ParameterizedTypeReference<ResultObject<PersonCO>> responseType =
            new ParameterizedTypeReference<>() {
            };
    private static ParameterizedTypeReference<ResultObject<List<PersonCO>>> listResponseType =
            new ParameterizedTypeReference<>() {
            };

    private TestRestTemplate restTemplate;

    @Autowired
    public ApplicationTests(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Test
    @Order(1)
    public void testCreatePerson() {
        final PersonCO personCO = new PersonCO();
        personCO.setFirstName(FIRST_NAME);
        personCO.setLastName(LAST_NAME);
        HttpEntity<PersonCO> request = new HttpEntity<>(personCO);

        ResponseEntity<ResultObject<PersonCO>> postResponse = restTemplate.exchange(getResourceUrl(), HttpMethod.POST,
                request, responseType);
        Assertions.assertNotNull(postResponse);
        Assertions.assertNotNull(postResponse.getBody());
        Assertions.assertNotNull(postResponse.getBody().getResult());
        Assertions.assertTrue(postResponse.getBody().getSuccess());

        PersonCO createdPersonCO = postResponse.getBody().getResult();
        Assertions.assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        Assertions.assertEquals(FIRST_NAME, createdPersonCO.getFirstName());
        Assertions.assertEquals(LAST_NAME, createdPersonCO.getLastName());

        id = createdPersonCO.getId();
    }

    @Test
    @Order(1)
    public void testCreatePersonWithoutLastName() {
        final PersonCO personCO = new PersonCO();
        personCO.setFirstName(FIRST_NAME);

        HttpEntity<PersonCO> request = new HttpEntity<>(personCO);

        ResponseEntity<ResultObject<PersonCO>> postResponse = restTemplate.exchange(getResourceUrl(), HttpMethod.POST,
                request, responseType);
        Assertions.assertNotNull(postResponse);
        Assertions.assertNotNull(postResponse.getBody());
        Assertions.assertFalse(postResponse.getBody().getSuccess());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    @Order(2)
    public void testGetPersonById() {
        ResponseEntity<ResultObject<PersonCO>> getResponse =
                restTemplate.exchange(getResourceUrl() + "/" + id.toString(), HttpMethod.GET,
                        null, responseType);
        Assertions.assertNotNull(getResponse);
        Assertions.assertNotNull(getResponse.getBody());
        Assertions.assertNotNull(getResponse.getBody().getResult());
        Assertions.assertNotNull(getResponse.getBody().getResult().getId());
        Assertions.assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }

    @Test
    @Order(3)
    public void testListPeopleByFirstNameStartsWithCaseInsensitive() {
        ResponseEntity<ResultObject<List<PersonCO>>> getResponse =
                restTemplate.exchange(getResourceUrl() + "/?firstName=TEST-", HttpMethod.GET,
                        null, listResponseType);
        Assertions.assertNotNull(getResponse);
        Assertions.assertNotNull(getResponse.getBody());
        Assertions.assertNotNull(getResponse.getBody().getResult());
        Assertions.assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Assertions.assertFalse(CollectionUtils.isEmpty(getResponse.getBody().getResult()));

        List<PersonCO> listPeopleResult = getResponse.getBody().getResult();
        PersonCO createdPerson =
                listPeopleResult.stream().filter(person -> id.equals(person.getId())).findFirst().orElse(null);
        Assertions.assertNotNull(createdPerson);
    }

    @Test
    @Order(3)
    public void testListPeopleByRandomFirstNameReturnsEmptyList() {
        final String randomString = UUID.randomUUID().toString();
        ResponseEntity<ResultObject<List<PersonCO>>> getResponse =
                restTemplate.exchange(getResourceUrl() + "/?firstName=" + randomString, HttpMethod.GET,
                        null, listResponseType);
        Assertions.assertNotNull(getResponse);
        Assertions.assertNotNull(getResponse.getBody());
        Assertions.assertTrue(CollectionUtils.isEmpty(getResponse.getBody().getResult()));
    }

    @Test
    @Order(3)
    public void testListPeopleEndWithFirstNameStartWithLastName() {
        ResponseEntity<ResultObject<List<PersonCO>>> getResponse =
                restTemplate.exchange(getResourceUrl() + "/?firstName=FirstName" + "&lastName=TEST-L",
                        HttpMethod.GET, null, listResponseType);
        Assertions.assertNotNull(getResponse);
        Assertions.assertNotNull(getResponse.getBody());
        Assertions.assertNotNull(getResponse.getBody().getResult());
        Assertions.assertFalse(CollectionUtils.isEmpty(getResponse.getBody().getResult()));

        List<PersonCO> listPeopleResult = getResponse.getBody().getResult();
        PersonCO createdPerson =
                listPeopleResult.stream().filter(person -> id.equals(person.getId())).findFirst().orElse(null);
        Assertions.assertNotNull(createdPerson);
    }

    @Test
    @Order(4)
    public void testUpdatePerson() {
        final PersonCO personCO = restTemplate.getForObject(getRootUrl() + "/" + id.toString(), PersonCO.class);
        personCO.setFirstName(FIRST_NAME_UPDATED);
        personCO.setLastName(LAST_NAME_UPDATED);
        HttpEntity<PersonCO> request = new HttpEntity<>(personCO);

        ResponseEntity<ResultObject<PersonCO>> putResponse =
                restTemplate.exchange(getResourceUrl() + "/" + id.toString(), HttpMethod.PUT,
                        request, responseType);
        Assertions.assertNotNull(putResponse);
        Assertions.assertNotNull(putResponse.getBody());
        Assertions.assertNotNull(putResponse.getBody().getResult());
        Assertions.assertNotNull(putResponse.getBody().getResult().getId());
        Assertions.assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        Assertions.assertTrue(putResponse.getBody().getSuccess());

        PersonCO updatedPerson = putResponse.getBody().getResult();
        Assertions.assertEquals(FIRST_NAME_UPDATED, updatedPerson.getFirstName());
        Assertions.assertEquals(LAST_NAME_UPDATED, updatedPerson.getLastName());
    }

    @Test
    @Order(5)
    public void testDeletePerson() {
        final PersonCO personCO = restTemplate.getForObject(getResourceUrl() + "/" + id.toString(), PersonCO.class);
        Assertions.assertNotNull(personCO);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(getResourceUrl() + "/" + id.toString(),
                HttpMethod.DELETE, null, Void.class);
        Assertions.assertNotNull(deleteResponse);
        Assertions.assertNull(deleteResponse.getBody());
        Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        try {
            restTemplate.getForObject(getResourceUrl() + "/" + id.toString(), PersonCO.class);
        } catch (final HttpClientErrorException e) {
            Assertions.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }

}
