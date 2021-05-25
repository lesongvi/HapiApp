package com.hapi.hapiservice.controllers;

import com.hapi.hapiservice.helpers.common.routeHelper;
import com.hapi.hapiservice.helpers.common.studentHelper;
import com.hapi.hapiservice.helpers.respository.StudentRepository;
import com.hapi.hapiservice.models.student.RestForm;
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

    private studentHelper studentEngine;

    @RequestMapping(value = {routeHelper.evaluateList}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String studentEvaluate (@RequestParam(value = "token", defaultValue = "") String token) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.studentEngine = new studentHelper(token, this.studentRepository, this.studentService);

        return this.studentEngine.getEvaluateList();
    }

    @RequestMapping(value = {routeHelper.evaluateView}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String studentEvaluate (@RequestParam(value = "token", defaultValue = "") String token, @RequestParam(value = "ticket", defaultValue = "") String ticket) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalAccessException {
        this.studentEngine = new studentHelper(token, this.studentRepository, this.studentService);

        return this.studentEngine.getEvaluateTicket(ticket);
    }

    @RequestMapping(value = {routeHelper.restList}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String studentTARestList (@RequestParam(value = "token", defaultValue = "") String token) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.studentEngine = new studentHelper(token, this.studentRepository, this.studentService);

        return this.studentEngine.getRestList();
    }

    /*@RequestMapping(value = {routeHelper.takeARest}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String studentTARest (@RequestParam(value = "token", defaultValue = "") String token, @RequestParam(value = "monhoc", defaultValue = "") String monhoc, @RequestParam(value = "giangvien", defaultValue = "") String giangvien, @RequestParam(value = "ngaynghi", defaultValue = "") String ngaynghi, @RequestParam(value = "lydo", defaultValue = "") String lydo, @RequestParam(value = "ca", defaultValue = "") List<String> ca) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.studentEngine = new studentHelper(token, this.studentRepository, this.studentService);
        RestForm rest = new RestForm().setMonHoc(monhoc).setGiangVien(giangvien).setCa(ca.toArray(new String[ca.size()])).setLyDo(lydo).setNgayNghi(ngaynghi);

        return this.studentEngine.takeARestByParams(rest);
    }*/

    @RequestMapping(value = {routeHelper.usDetailData}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String studentDetail (@RequestParam(value = "token", defaultValue = "") String token) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        this.studentEngine = new studentHelper(token, this.studentRepository, this.studentService);

        return this.studentEngine.getStudentDetail();
    }

    @RequestMapping(value = {routeHelper.routeNationData}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String routeNationData (@RequestParam(value = "token", defaultValue = "") String token) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        this.studentEngine = new studentHelper(token, this.studentRepository, this.studentService);

        return this.studentEngine.getRouteNation();
    }

    @RequestMapping(value = {routeHelper.surveyList}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String studentSurveyList (@RequestParam(value = "token", defaultValue = "") String token) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        this.studentEngine = new studentHelper(token, this.studentRepository, this.studentService);

        return this.studentEngine.getSurveyList();
    }

    @RequestMapping(value = {routeHelper.surveyDetail}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String studentSurveyDetail (@RequestParam(value = "token", defaultValue = "") String token, @RequestParam(value = "is_dang_lam", defaultValue = "true") String isDangLam, @RequestParam(value = "phieu_danh_gia_id", defaultValue = "") String phieuDanhGiaId) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        this.studentEngine = new studentHelper(token, this.studentRepository, this.studentService);

        return this.studentEngine.getSurveyDetail(isDangLam, phieuDanhGiaId);
    }

    @RequestMapping(value = {routeHelper.surveyRequest}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String studentSurveyRequest (@RequestParam(value = "token", defaultValue = "") String token, @RequestParam(value = "ma_phieu_id", defaultValue = "") String maPhieuId, @RequestParam(value = "option", defaultValue = "") String option) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        this.studentEngine = new studentHelper(token, this.studentRepository, this.studentService);

        return this.studentEngine.takeSurvey(maPhieuId, option);
    }
}
