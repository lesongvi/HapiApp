package com.hapi.hapiservice.controllers;

import com.hapi.hapiservice.helpers.common.browserHelper;
import com.hapi.hapiservice.helpers.common.routeHelper;
import com.hapi.hapiservice.helpers.common.studentHelper;
import com.hapi.hapiservice.helpers.respository.StudentRepository;
import com.hapi.hapiservice.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = {routeHelper.evaluateList}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String studentEvaluate (@RequestParam(value = "token", defaultValue = "") String token) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        studentHelper studentEngine = new studentHelper(token, this.studentRepository, this.studentService);

        return studentEngine.getEvaluateList();
    }

    @RequestMapping(value = {routeHelper.evaluateView}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String studentEvaluate (@RequestParam(value = "token", defaultValue = "") String token, @RequestParam(value = "ticket", defaultValue = "") String ticket) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalAccessException {
        studentHelper studentEngine = new studentHelper(token, this.studentRepository, this.studentService);

        return studentEngine.getEvaluateTicket(ticket);
    }
}
