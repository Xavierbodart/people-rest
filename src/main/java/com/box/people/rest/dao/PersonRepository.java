package com.box.people.rest.dao;

import com.box.people.rest.model.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface PersonRepository extends JpaRepository<PersonEntity, String> {

    @Query(value = "SELECT p.* FROM PEOPLE p " +
            "WHERE (:firstName IS NULL OR lower(p.first_name) LIKE concat(lower(:firstName),'%') " +
            "   OR lower(p.first_name) LIKE concat('%',lower(:firstName))) " +
            "AND (:lastName IS NULL OR lower(p.last_name) LIKE concat(lower(:lastName ),'%') " +
            "   OR lower(p.last_name) LIKE concat('%',lower(:lastName )))",
            nativeQuery = true)
    List<PersonEntity> filterPeopleByFirstNameAndLastName(@Param("firstName") String firstName,
                                                          @Param("lastName") String lastName);
}
