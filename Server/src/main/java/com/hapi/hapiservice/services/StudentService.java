package com.hapi.hapiservice.services;

import com.hapi.hapiservice.helpers.StudentRepository;
import com.hapi.hapiservice.models.Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    protected StudentRepository studentRepository;

    public void save(Students students) {
        studentRepository.save(students);
    }

    public Optional<Students> findById(int stdntId) {
        return studentRepository.findById(stdntId);
    }

    public Students getStudent(int stdntId) {
        return (Students)studentRepository.findById(stdntId).get();
    }

    public Students getStudentByToken(String token) {
        return (Students)studentRepository.findByToken(token);
    }

    public Students saveAndGetStudent(Students stdnt) {
        Students _stdnt = studentRepository.findById(stdnt.getSid()).get();
        _stdnt.setPwd(stdnt.getPwd());
        _stdnt.setEmail(stdnt.getEmail());
        _stdnt.setSdt(1, stdnt.getSdt(1));
        _stdnt.setSdt(2, stdnt.getSdt(2));
        this.save(_stdnt);

        return this.getStudent(stdnt.getSid());
    }
}
