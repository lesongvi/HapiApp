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

    @RequestMapping(value = {routeHelper.studentAuth}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String authenticate (@RequestParam(value = "sid", defaultValue = "") int sid, @RequestParam(value = "pwd", defaultValue = "") String pwd) throws IOException {
        browserHelper studentBasicTest = new browserHelper(sid, pwd, this.studentRepository, this.studentService);
        Optional userVerify = this.studentService.findById(sid);

        return studentBasicTest.conAuth(userVerify.isPresent());
    }

    @RequestMapping(value = {routeHelper.getSemester}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSemester (@RequestParam(value = "token", defaultValue = "") String token) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        browserHelper studentBasicTest = new browserHelper(token, this.studentRepository, this.studentService);

        return studentBasicTest.getSemesterList();
    }

    @RequestMapping(value = {routeHelper.getWeek}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWeek (@RequestParam(value = "token", defaultValue = "") String token, @RequestParam(value = "semesterId", defaultValue = "") String semesterId) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        browserHelper studentBasicTest = new browserHelper(token, this.studentRepository, this.studentService);

        return studentBasicTest.getWeekList(semesterId);
    }

    @RequestMapping(value = {routeHelper.getSchedule}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSchedule (@RequestParam(value = "token", defaultValue = "") String token, @RequestParam(value = "semesterId", defaultValue = "") String semesterId, @RequestParam(value = "weekId", defaultValue = "") String weekId) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, ParseException {
        browserHelper studentBasicTest = new browserHelper(token, this.studentRepository, this.studentService);

        return studentBasicTest.getScheduleDetail(semesterId, weekId);
    }

    @RequestMapping(value = {routeHelper.getCurrentPoint}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCurrentPoint (@RequestParam(value = "token", defaultValue = "") String token) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        browserHelper studentBasicTest = new browserHelper(token, this.studentRepository, this.studentService);

        return studentBasicTest.getCurrentPoint();
    }

    @RequestMapping(value = {routeHelper.getPSemeterList}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPSemeterList (@RequestParam(value = "token", defaultValue = "") String token) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        browserHelper studentBasicTest = new browserHelper(token, this.studentRepository, this.studentService);

        return studentBasicTest.getPointListSemester("");
    }

    @RequestMapping(value = {routeHelper.getPSemeterDetail}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSemesterPoint (@RequestParam(value = "token", defaultValue = "") String token, @RequestParam(value = "semesterId", defaultValue = "") String semesterId) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        browserHelper studentBasicTest = new browserHelper(token, this.studentRepository, this.studentService);

        return studentBasicTest.getPointListSemester(semesterId);
    }


}
