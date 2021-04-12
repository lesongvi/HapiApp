package com.hapi.hapiservice.helpers.common;

import com.google.gson.Gson;
import com.hapi.hapiservice.helpers.respository.StudentRepository;
import com.hapi.hapiservice.models.schedule.Students;
import com.hapi.hapiservice.models.student.*;
import com.hapi.hapiservice.services.StudentService;
import org.apache.commons.io.IOUtils;
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
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

        try {
            InputStream instream = this.sRequest(this.definedStr.studentAuthApi_PRODUCTION(), "{\"app\":\"MOBILE_HUTECH\", \"captcha\": null, \"diuu\": 123, \"password\": \"" + studentPassword + "\", \"username\": \"" + studentId + "\"}");
            this.token = new Gson().fromJson(IOUtils.toString(instream, StandardCharsets.UTF_8.name()), loginResponse.class).getToken();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public String getEvaluateList() throws IOException {
        InputStream instream = null;
        try {
            instream = this.sRequest(this.definedStr.studentEvaluateSearchByStudent_PRODUCTION(), "{\"page\": 1, \"per_page\": 20}");
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return new Gson().toJson(new Gson().fromJson(IOUtils.toString(instream, StandardCharsets.UTF_8.name()), evaluatePaginate.class).getResult());
        }
    }

    private InputStream sRequest(String req, String content) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost actionUrl = new HttpPost(req);

        StringEntity input =
                new StringEntity(content);
        actionUrl = this.initialAction(input, actionUrl);

        HttpResponse response = httpClient.execute(actionUrl);

        //if (response.getStatusLine().getStatusCode() != 200)
            //throw new NullPointerException("Máy chủ trả lại lỗi!");

        return response.getEntity().getContent();
    }

    public String takeARestByParams(RestForm rest) {
        evaluateTicketResultItem[] qlist = null;
        try {
            InputStream instream = this.sRequest(this.definedStr.studentTakeaRest_PRODUCTION(), "{\"mon_hoc\": " + rest.getMonHoc() + ", \"giang_vien\": " + rest.getGiangVien() + ", \"ngay_nghi\": " + rest.getNgayNghi() + ", \"ly_do\": " + rest.getLyDo() + ", \"ca\": " + rest.getCaHoc() + "}");
            System.out.println(IOUtils.toString(instream, StandardCharsets.UTF_8.name()));
            qlist = new Gson().fromJson(IOUtils.toString(instream, StandardCharsets.UTF_8.name()), evaluateTicket.class).getResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return new Gson().toJson(qlist);
        }
    }

    public String getRestList() {
        studentRestList[] rlist = null;
        try {
            InputStream instream = this.sRequest(this.definedStr.studentRestList_PRODUCTION(), "{\"page\": " + 1 + ", \"per_page\": " + 10 + "}");
            rlist = new Gson().fromJson(IOUtils.toString(instream, StandardCharsets.UTF_8.name()), restListResponse.class).getResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return new Gson().toJson(rlist);
        }
    }

    public String getEvaluateTicket(String ticketId) {
        evaluateTicketResultItem[] qlist = null;
        try {
            InputStream instream = this.sRequest(this.definedStr.studentEvaluateGetTicket_PRODUCTION(), "{\"phieu_danh_gia_id\": " + ticketId + "}");
            qlist = new Gson().fromJson(IOUtils.toString(instream, StandardCharsets.UTF_8.name()), evaluateTicket.class).getResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return new Gson().toJson(qlist);
        }
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