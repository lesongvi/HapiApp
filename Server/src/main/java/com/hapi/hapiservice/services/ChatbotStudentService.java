package com.hapi.hapiservice.services;

import com.hapi.hapiservice.helpers.respository.ChatbotStudentRepository;
import com.hapi.hapiservice.models.bot.ChatbotStudents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatbotStudentService {
    @Autowired
    protected ChatbotStudentRepository chatbotStudentRepository;

    public void save(ChatbotStudents cbstudents) {
        chatbotStudentRepository.save(cbstudents);
    }

    public Optional<ChatbotStudents> findById(String fbid) {
        return chatbotStudentRepository.findById(fbid);
    }
}
