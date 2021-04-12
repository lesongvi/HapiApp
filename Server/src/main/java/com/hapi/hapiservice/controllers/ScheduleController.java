package com.hapi.hapiservice.controllers;

import com.hapi.hapiservice.helpers.common.snapshotHelper;
import com.hapi.hapiservice.helpers.respository.StudentRepository;
import com.hapi.hapiservice.helpers.common.browserHelper;
import com.hapi.hapiservice.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.hapi.hapiservice.helpers.common.routeHelper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Optional;

@RestController
public class ScheduleController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;

    private browserHelper studentBasicTest;

    @RequestMapping(value = {routeHelper.studentAuth}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String authenticate (@RequestParam(value = "sid", defaultValue = "") int sid, @RequestParam(value = "pwd", defaultValue = "") String pwd) throws IOException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        this.studentBasicTest = new browserHelper(sid, pwd, this.studentRepository, this.studentService);
        Optional userVerify = this.studentService.findById(sid);

        return this.studentBasicTest.conAuth(userVerify.isPresent());
    }

    @RequestMapping(value = {routeHelper.getSemester}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSemester (@RequestParam(value = "token", defaultValue = "") String token) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.studentBasicTest = new browserHelper(token, this.studentRepository, this.studentService);

        return this.studentBasicTest.getSemesterList();
    }

    @RequestMapping(value = {routeHelper.getWeek}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWeek (@RequestParam(value = "token", defaultValue = "") String token, @RequestParam(value = "semesterId", defaultValue = "") String semesterId) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.studentBasicTest = new browserHelper(token, this.studentRepository, this.studentService);

        return this.studentBasicTest.getWeekList(semesterId);
    }

    @RequestMapping(value = {routeHelper.getSchedule}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSchedule (@RequestParam(value = "token", defaultValue = "") String token, @RequestParam(value = "semesterId", defaultValue = "") String semesterId, @RequestParam(value = "weekId", defaultValue = "") String weekId) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, ParseException {
        this.studentBasicTest = new browserHelper(token, this.studentRepository, this.studentService);

        return this.studentBasicTest.getScheduleDetail(semesterId, weekId);
    }

    @RequestMapping(value = {routeHelper.getCurrentPoint}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCurrentPoint (@RequestParam(value = "token", defaultValue = "") String token) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.studentBasicTest = new browserHelper(token, this.studentRepository, this.studentService);

        return this.studentBasicTest.getCurrentPoint();
    }

    @RequestMapping(value = {routeHelper.getPSemeterList}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPSemeterList (@RequestParam(value = "token", defaultValue = "") String token) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.studentBasicTest = new browserHelper(token, this.studentRepository, this.studentService);

        return this.studentBasicTest.getPointListSemester("");
    }

    @RequestMapping(value = {routeHelper.getPSemeterDetail}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSemesterPoint (@RequestParam(value = "token", defaultValue = "") String token, @RequestParam(value = "semesterId", defaultValue = "") String semesterId) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.studentBasicTest = new browserHelper(token, this.studentRepository, this.studentService);

        return this.studentBasicTest.getPointListSemester(semesterId);
    }


}
