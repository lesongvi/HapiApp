package com.hapi.hapiservice.helpers.respository;

import com.hapi.hapiservice.models.bot.ChatbotStudents;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatbotStudentRepository extends CrudRepository<ChatbotStudents, String> {
}
