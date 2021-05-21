package com.hapi.hapiservice.helpers.common;

import com.google.gson.Gson;
import com.hapi.hapiservice.helpers.respository.StudentRepository;
import com.hapi.hapiservice.models.app.CloudAResponse;
import com.hapi.hapiservice.services.StudentService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AppHelper {
    private StudentRepository studentRepository;
    private StudentService studentService;
    protected EncryptHelper encryptEngine = new EncryptHelper();
    private String token;
    Logger logger = LoggerFactory.getLogger(AppHelper.class);
    private stringHelper definedStr = new stringHelper();

    public AppHelper(
            String token,
            StudentRepository studentRepository,
            StudentService studentService) {
        this.token = token;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    public String requestAvatarModify(String base64)  {
        CloudAResponse objResponse = null;
        try {
            InputStream instream = this.sRequest(this.definedStr.notevnCloudA1_PRODUCTION(), "{\"base64\":\"" + URLEncoder.encode(base64, StandardCharsets.UTF_8.toString()) + "\", \"ImageName\": \"hapi_noname\"}");
            objResponse = new Gson().fromJson(IOUtils.toString(instream, StandardCharsets.UTF_8.name()), CloudAResponse.class);
            if (objResponse != null && objResponse.getType().contains(".png")) {
                this.studentService.updateStudentAvatar("https://cdn.notevn.com/" + objResponse.getFile_name() + objResponse.getType(), this.token);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return new Gson().toJson(objResponse);
        }
    }

    private InputStream sRequest(String req, String content) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost actionUrl = new HttpPost(req);

        actionUrl.setEntity(new StringEntity(content));

        HttpResponse response = httpClient.execute(actionUrl);

        return response.getEntity().getContent();
    }

}
