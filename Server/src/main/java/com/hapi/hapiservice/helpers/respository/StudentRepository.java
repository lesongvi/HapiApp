package com.hapi.hapiservice.helpers.respository;

import com.hapi.hapiservice.models.schedule.Students;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Students, Integer> {
    Students findByToken(String token);
}
