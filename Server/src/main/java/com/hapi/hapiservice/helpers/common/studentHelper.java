package com.hapi.hapiservice.helpers.common;

import com.google.gson.Gson;
import com.hapi.hapiservice.helpers.respository.StudentRepository;
import com.hapi.hapiservice.models.schedule.Students;
import com.hapi.hapiservice.models.student.*;
import com.hapi.hapiservice.services.StudentService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

public class studentHelper {
    private StudentRepository studentRepository;
    private StudentService studentService;
    protected EncryptHelper encryptEngine = new EncryptHelper();
    private String token;
    Logger logger = LoggerFactory.getLogger(studentHelper.class);
    private stringHelper definedStr = new stringHelper();

    public studentHelper(
            String token,
            StudentRepository studentRepository,
            StudentService studentService) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        this.token = token;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
        this.genJWTToken();
    }

    public void genJWTToken() throws IOException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Students tokenInfo = this.studentService.getStudentByToken(this.token);

        int studentId = tokenInfo.getSid();
        String studentPassword = this.encryptEngine.pleaseHelpViHackThis(tokenInfo.getPwd());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost actionUrl = new HttpPost("https://api.hutech.edu.vn/authentication/api/auth/login");

        StringEntity input = new StringEntity("{\"app\":\"MOBILE_HUTECH\", \"captcha\": null, \"diuu\": 123, \"password\": \"" + studentPassword + "\", \"username\": \"" + studentId + "\"}");
        actionUrl = this.initialAction(input, actionUrl);

        HttpResponse response = httpClient.execute(actionUrl);

        HttpEntity entity = response.getEntity();

        InputStream instream = entity.getContent();

        if (response.getStatusLine().getStatusCode() != 200)
            this.token = null;
        else
            this.token = new Gson().fromJson(IOUtils.toString(instream, StandardCharsets.UTF_8.name()), loginResponse.class).getToken();
    }

    public String getEvaluateList() throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost actionUrl = new HttpPost(this.definedStr.studentEvaluateSearchByStudent_PRODUCTION());

        StringEntity input = new StringEntity("{\"page\": 1, \"per_page\": 20}");
        actionUrl = this.initialAction(input, actionUrl);

        HttpResponse response = httpClient.execute(actionUrl);

        HttpEntity entity = response.getEntity();

        InputStream instream = entity.getContent();

        if (response.getStatusLine().getStatusCode() != 200)
            return null;
        else
            return new Gson().toJson(new Gson().fromJson(IOUtils.toString(instream, StandardCharsets.UTF_8.name()), evaluatePaginate.class).getResult());
    }

    public String getEvaluateTicket(String ticketId) throws IOException, IllegalAccessException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost actionUrl = new HttpPost(this.definedStr.studentEvaluateGetTicket_PRODUCTION());

        StringEntity input = new StringEntity("{\"phieu_danh_gia_id\": " + ticketId + "}");
        actionUrl = this.initialAction(input, actionUrl);

        HttpResponse response = httpClient.execute(actionUrl);

        HttpEntity entity = response.getEntity();

        InputStream instream = entity.getContent();

        evaluateTicketResultItem[] qlist = new Gson().fromJson(IOUtils.toString(instream, StandardCharsets.UTF_8.name()), evaluateTicket.class).getResult();

        if (response.getStatusLine().getStatusCode() != 200)
            return null;
        else
            return new Gson().toJson(qlist);
    }

    public HttpPost initialAction(StringEntity input, HttpPost actionUrl) {
        input.setContentType("application/json");
        actionUrl.setHeader("access-control-allow-headers", "Content-Type, Authorization, Content-Length, X-Requested-With, app-key");
        actionUrl.setHeader("access-control-allow-methods", "GET,PUT,POST,DELETE,OPTIONS");
        actionUrl.setHeader("access-control-allow-origin", "*");
        if (this.token.length() != 0)
            actionUrl.setHeader("authorization", "JWT "+ this.token);
        actionUrl.setHeader("Content-Type", "application/json");
        actionUrl.setHeader("Accept", "application/json");
        actionUrl.setEntity(input);

        return actionUrl;
    }
}
