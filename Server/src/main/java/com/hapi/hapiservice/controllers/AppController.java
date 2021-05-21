package com.hapi.hapiservice.controllers;

import com.hapi.hapiservice.helpers.common.AppHelper;
import com.hapi.hapiservice.helpers.common.routeHelper;
import com.hapi.hapiservice.helpers.respository.StudentRepository;
import com.hapi.hapiservice.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;

    private AppHelper appEngine;

    @RequestMapping(value = {routeHelper.avatarModify}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String avatarModify (@RequestParam(value = "token", defaultValue = "") String token, @RequestParam(value = "base64", defaultValue = "") String base64) {
        this.appEngine = new AppHelper(token, this.studentRepository, this.studentService);

        return this.appEngine.requestAvatarModify(base64);
    }
}
