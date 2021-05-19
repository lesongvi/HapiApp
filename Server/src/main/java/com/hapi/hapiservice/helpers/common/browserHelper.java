package com.hapi.hapiservice.helpers.common;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.hapi.hapiservice.helpers.respository.StudentRepository;
import com.hapi.hapiservice.models.schedule.*;
import com.hapi.hapiservice.services.StudentService;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.net.CookieManager;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class browserHelper extends stuffHelper {
    final stringHelper definedStr = new stringHelper();
    final WebClient webClient = this.initial();
    protected int studentId;
    protected String studentPassword;
    private StudentRepository studentRepository;
    private StudentService studentService;
    private static final SecureRandom secureRand = new SecureRandom();
    private static final Base64.Encoder base64 = Base64.getUrlEncoder();
    protected String token;
    protected EncryptHelper encryptEngine = new EncryptHelper();
    Logger logger = LoggerFactory.getLogger(browserHelper.class);

    public browserHelper(
            int studentId,
            String studentPassword,
            StudentRepository studentRepository,
            StudentService studentService) {
        this.studentId = studentId;
        this.studentPassword = studentPassword;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    public browserHelper(
            String token,
            StudentRepository studentRepository,
            StudentService studentService) {
        this(0, null, studentRepository, studentService);
        this.token = token;
    }

    public browserHelper() {
        this(null, null, null);
    }

    public String conAuth(boolean isSaved) throws MalformedURLException {
        Gson gson = new Gson();
        authSuccess response = new authSuccess(true, "", "");

        if ((this.studentId + "").length() != 10)
            return gson.toJson(new errorResponse(true, "Tài khoản sinh viên không hợp lệ", 404));

        ArrayList<String> studntMP = this.getMailAndPhoneNum();

        logger.debug(studntMP.size()+"");

        try {
            if (isSaved) {
                Students stdnt = new Students();
                stdnt.setId(this.studentId);
                stdnt.setPwd(this.encryptEngine.pleaseHelpViSecureThis(this.studentPassword));
                stdnt.setEmail(studntMP.get(0));
                stdnt.setSdt(1, studntMP.get(1));
                stdnt.setSdt(2, studntMP.get(2));
                Students stdunt = studentService.saveAndGetStudent(stdnt);

                response = new authSuccess(false, stdunt.getName(), stdunt.getToken());
            } else {
                String genToken = this.generateStdntToken();
                Students stdnt = new Students();
                stdnt.setId(this.studentId);
                stdnt.setPwd(this.encryptEngine.pleaseHelpViSecureThis(this.studentPassword));
                stdnt.setName(this.getSName());
                stdnt.setEmail(studntMP.get(0));
                stdnt.setSdt(1, studntMP.get(1));
                stdnt.setSdt(2, studntMP.get(2));
                stdnt.setToken(genToken);
                studentRepository.save(stdnt);

                response = new authSuccess(false, this.getSName(), genToken);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return gson.toJson(new errorResponse(true, "Có lỗi đã xảy ra!", 404));
        } finally {
            return gson.toJson(response);
        }
    }

    public static String generateStdntToken() {
        byte[] randomBytes = new byte[24];
        secureRand.nextBytes(randomBytes);
        return base64.encodeToString(randomBytes);
    }

    public WebClient getLoginVS () {
        try {
            Matcher check = this.processingCredential(this.definedStr.invalidCredentialsPattern_PRODUCTION());
            if (check.find()) {
                this.webClient.close();
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return this.webClient;
        }
    }

    public boolean isServerReloading() throws IOException {
        URL actionUrl = new URL(this.definedStr.defaultPage_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);

        defaultPage = this.setAdditionalHeader(defaultPage);

        HtmlPage page = this.requestLogin()
                .getPage(defaultPage);

        Pattern pattern = Pattern.compile(this.definedStr.serverReloadPattern_PRODUCTION());
        Matcher checkReloading = pattern.matcher(page.asXml());

        return checkReloading.find();
    }

    public Matcher processingCredential(String regex) throws IOException {
        Object _lock = new Object();
        URL actionUrl = new URL(this.definedStr.defaultPage_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);
        String parseBody;
        Pattern pattern;

        defaultPage = this.setAdditionalHeader(defaultPage);

        synchronized (_lock) {
            HtmlPage page = null;
            page = this.requestLogin()
                    .getPage(defaultPage);

            parseBody = getString(page);

            pattern = Pattern.compile(regex);
        }

        return pattern.matcher(parseBody);
    }

    private String getString(HtmlPage page) {
        String parseBody;
        parseBody = this.passCredentials(page).asXml();
        return parseBody;
    }

    private HtmlPage passCredentials(HtmlPage page) {
        try {
            HtmlInput logInput = page.getHtmlElementById(this.definedStr.loginId_PRODUCTION());
            HtmlInput pwdInput = page.getHtmlElementById(this.definedStr.loginPwdId_PRODUCTION());

            logInput.setValueAttribute(this.studentId + "");
            pwdInput.setValueAttribute(this.studentPassword);

            page = page.getHtmlElementById(this.definedStr.loginBtnId_PRODUCTION()).click();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return page;
        }
    }

    public String loginAndPattern(String regex, int groupNum) throws IOException {
        Matcher searchResult = this.processingCredential(regex);
        if (searchResult.find()) {
            webClient.close();
            return searchResult.group(groupNum);
        }
        return "";
    }

    public String[] getVS(String initialUrl) throws MalformedURLException {
        URL actionUrl = new URL(initialUrl);
        WebRequest defaultPage = new WebRequest(actionUrl);

        HtmlPage page = null;
        try {
            page = (HtmlPage) this.webClient
                    .getPage(defaultPage);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        webClient.close();
        return new String[]{ page.getElementById("__VIEWSTATE").getAttribute("value"), page.getElementById("__VIEWSTATEGENERATOR").getAttribute("value") };
    }

    public String getSName() throws MalformedURLException {
        URL actionUrl = new URL(this.definedStr.schedulePage_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);
        String _studntName = null;

        try {
            HtmlPage page = (HtmlPage) this.webClient
                    .getPage(defaultPage);

            DomElement helloStudnt = page.getElementById(definedStr.helloStudentId_PRODUCTION());
            if (helloStudnt.getTagName().toLowerCase().equals("span")) {
                String _xmlDefault = this.removeTheDEGap(helloStudnt);
                Matcher defaultMyView = this.patternSearch(this.definedStr.studentNamePattern_PRODUCTION(), _xmlDefault);
                if (!defaultMyView.find())
                    return null;
                _studntName = defaultMyView.group(1);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return _studntName;
        }
    }


    public WebClient requestLogin() throws IOException {
        URL actionUrl = new URL(this.definedStr.defaultPage_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);

        defaultPage = this.setAdditionalHeader(defaultPage);

        HtmlPage page;
        try {
            page = this.webClient
                    .getPage(defaultPage);

            this.passCredentials(page);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return this.webClient;
    }

    private WebRequest setAdditionalHeader(WebRequest wr) {
        wr.setAdditionalHeader("User-Agent", this.definedStr.userAgentDefault_PRODUCTION());
        wr.setAdditionalHeader("Accept-Encoding", this.definedStr.acceptEncoding_PRODUCTION());
        wr.setAdditionalHeader("Accept-Language", this.definedStr.acceptLanguage_PRODUCTION());
        wr.setAdditionalHeader("Accept", this.definedStr.acceptDataType_PRODUCTION());
        wr.setAdditionalHeader("Origin", this.definedStr.reqOrigin_PRODUCTION());
        return wr;
    }

    public ArrayList<String> getMailAndPhoneNum() throws MalformedURLException {
        URL actionUrl = new URL(this.definedStr.userInfoChangerUrl_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);
        ArrayList<String> _infoBack = new ArrayList<String>();

        defaultPage = this.setAdditionalHeader(defaultPage);

        try {
            HtmlPage page = this.requestLogin()
                    .getPage(defaultPage);

            DomElement studntMailSlct = page.getElementById(definedStr.studentEmailId_PRODUCTION());
            DomElement studntSdt1Slct = page.getElementById(definedStr.studentSdt1Id_PRODUCTION());
            DomElement studntSdt2Slct = page.getElementById(definedStr.studentSdt2Id_PRODUCTION());

            if (studntMailSlct.getTagName().toLowerCase().equals("input")) {
                _infoBack.add(studntMailSlct.getAttribute("value"));
            }
            if (studntSdt1Slct.getTagName().toLowerCase().equals("input")) {
                _infoBack.add(studntSdt1Slct.getAttribute("value"));
            }
            if (studntSdt2Slct.getTagName().toLowerCase().equals("input")) {
                _infoBack.add(studntSdt2Slct.getAttribute("value"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return _infoBack;
        }
    }

    public static String showCookies(String websiteURL)  throws IOException {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        URL url = new URL(websiteURL);
        URLConnection urlConnection = url.openConnection();
        urlConnection.getContent();

        CookieStore cookieStore = cookieManager.getCookieStore();

        return cookieStore.getCookies().get(0).getValue();
    }

    public WebClient verifyToken() throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Students tokenInfo = this.studentService.getStudentByToken(this.token);

        this.studentId = tokenInfo.getSid();
        this.studentPassword = this.encryptEngine.pleaseHelpViHackThis(tokenInfo.getPwd());

        return this.getLoginVS();
    }

    public String getSemesterList() throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        WebClient webClient = this.verifyToken();

        URL actionUrl = new URL(this.definedStr.schedulePage_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);
        ArrayList<semesterResponse> semesterMap = new ArrayList();

        Gson gson = new Gson();

        defaultPage = this.setAdditionalHeader(defaultPage);

        try {
            HtmlPage page = (HtmlPage) webClient
                    .getPage(defaultPage);

            DomElement semesterOpt = page.getElementById(definedStr.semesterOptId_PRODUCTION());

            semesterMap = this.semesterProcess(semesterOpt);

        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            webClient.close();
            return gson.toJson(semesterMap);
        }
    }

    public ArrayList<semesterResponse> getSemesterArr() throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        WebClient webClient = this.verifyToken();

        URL actionUrl = new URL(this.definedStr.schedulePage_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);
        ArrayList<semesterResponse> semesterMap = new ArrayList();

        defaultPage = this.setAdditionalHeader(defaultPage);

        try {
            HtmlPage page = (HtmlPage) webClient
                    .getPage(defaultPage);

            DomElement semesterOpt = page.getElementById(definedStr.semesterOptId_PRODUCTION());

            semesterMap = this.semesterProcess(semesterOpt);

        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            webClient.close();
            return semesterMap;
        }
    }

    private ArrayList<semesterResponse> semesterProcess(DomElement semesterOpt) {
        String[] _semesters = new String[] {};
        ArrayList<semesterResponse> semesterMap = new ArrayList<semesterResponse>();

        if(semesterOpt.getTagName().toLowerCase().equals("select")) {
            String _xmlSemester = this.removeTheDEGap(semesterOpt);
            Matcher semesterView = this.patternSearch(this.definedStr.semesterOptPattern_PRODUCTION(), _xmlSemester);
            if (!semesterView.find())
                return null;
            _semesters = semesterView
                    .group(2)
                    .replace("<option selected=\"selected\" value=\"", "")
                    .replace("\"", "")
                    .replace("</option>", "")
                    .split("<option value=", -1);
        }

        //Thread.sleep(1500);

        for (String tStr : _semesters){
            String singleSemester[] = tStr.split(">", -1);
            semesterMap.add(semesterMap.size(), new semesterResponse(singleSemester[0], singleSemester[1]));
        }

        return semesterMap;
    }

    public String getWeekList(String currSemester) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Gson gson = new Gson();

        return gson.toJson(this.getWeekArr(currSemester));
    }

    public ArrayList<weekResponse> getWeekArr(String currSemester) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        WebClient webClient = this.verifyToken();
        WebRequest defaultPage;

        if (currSemester != null)
            defaultPage = this.selectSemesterOpt(currSemester);
        else {
            URL actionUrl = new URL(this.definedStr.schedulePage_PRODUCTION());
            defaultPage = new WebRequest(actionUrl);
        }

        defaultPage = this.setAdditionalHeader(defaultPage);

        HtmlPage page = null;
        try {
            page = (HtmlPage) webClient
                    .getPage(defaultPage);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        DomElement semesterOpt = page.getElementById(definedStr.weekOptId_PRODUCTION());

        webClient.close();
        return this.weekProcessing(semesterOpt);
    }

    public weekResponse findSelectedWeek() throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, MalformedURLException, ParseException {
        WebClient webClient = this.verifyToken();
        Object _lock = new Object();

        URL actionUrl = new URL(this.definedStr.schedulePage_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);

        defaultPage = this.setAdditionalHeader(defaultPage);

        HtmlPage page = null;
        try {
            page = (HtmlPage) webClient
                    .getPage(defaultPage);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        DomElement semesterOpt = page.getElementById(definedStr.weekOptId_PRODUCTION());

        synchronized (_lock) {
            if(semesterOpt.getTagName().toLowerCase().equals("select")) {
                String _xmlWeek = this.removeTheDEGap(semesterOpt);
                Matcher weekView = this.patternSearch(this.definedStr.selectedMiniOptPattern_PRODUCTION(), _xmlWeek);
                if (weekView.find())
                {
                    String tempStr = weekView.group(1);
                    Matcher weekNum = this.patternSearch(this.definedStr.weekNumOptPattern_PRODUCTION(), tempStr);
                    Matcher startDate = this.patternSearch(this.definedStr.startDateOptPattern_PRODUCTION(), tempStr);
                    Matcher endDate = this.patternSearch(this.definedStr.endDateOptPattern_PRODUCTION(), tempStr);
                    if (weekNum.find() && startDate.find() && endDate.find()) {
                        return new weekResponse(startDate.group(1), endDate.group(1), weekNum.group(1), tempStr);
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<weekResponse> weekProcessing(DomElement semesterOpt) {
        ArrayList<weekResponse> _allSWeek = new ArrayList<weekResponse>();
        if(semesterOpt.getTagName().toLowerCase().equals("select")) {
            String _xmlWeek = this.removeTheDEGap(semesterOpt);
            Matcher weekView = this.patternSearch(this.definedStr.miniOptPattern_PRODUCTION(), _xmlWeek);
            while (weekView.find()) {
                String tempStr = weekView.group(1);
                Matcher weekNum = this.patternSearch(this.definedStr.weekNumOptPattern_PRODUCTION(), tempStr);
                Matcher startDate = this.patternSearch(this.definedStr.startDateOptPattern_PRODUCTION(), tempStr);
                Matcher endDate = this.patternSearch(this.definedStr.endDateOptPattern_PRODUCTION(), tempStr);
                if (weekNum.find() && startDate.find() && endDate.find()) {
                    try {
                        _allSWeek.add(new weekResponse(startDate.group(1), endDate.group(1), weekNum.group(1), tempStr));
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
        return _allSWeek;
    }

    public String getScheduleDetail(String currSemester, String currWeek) throws IOException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, ParseException {
        Gson gson = new Gson();

        return gson.toJson(this.getScheduleDetailArr(currSemester, currWeek));
    }

    public ArrayList<scheduleReponse> getScheduleDetailArr(String currSemester, String currWeek) throws IOException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, ParseException {
        WebClient webClient = this.verifyToken();
        WebRequest defaultPage;
        ArrayList<scheduleReponse> _fullWSchedule = new ArrayList<scheduleReponse>();

        if (currSemester != null && currWeek != null)
            defaultPage = this.selectWeekOpt(currSemester, currWeek);
        else {
            URL actionUrl = new URL(this.definedStr.schedulePage_PRODUCTION());
            defaultPage = new WebRequest(actionUrl);
        }

        defaultPage = this.setAdditionalHeader(defaultPage);

        HtmlPage page = (HtmlPage) webClient
                .getPage(defaultPage);

        if (currSemester != null && currWeek != null) page = this.getScheduleDetailByVS(webClient, page.getElementById("__VIEWSTATE").getAttribute("value"), page.getElementById("__VIEWSTATEGENERATOR").getAttribute("value"), currSemester, currWeek);

        DomElement scheduleTable = page.getElementById(definedStr.scheduleTableId_PRODUCTION());
        if(scheduleTable.getTagName().toLowerCase().equals("table")) {
            String _xmlSchedule = this.removeTheDEGap(scheduleTable);
            Matcher scheduleView = this.patternSearch(this.definedStr.schedulePattern_PRODUCTION(), _xmlSchedule);

            while (scheduleView.find()) {
                String[] tempStr = scheduleView
                        .group(1)
                        //.replaceAll(this.definedStr.scheduleDescRemove_PRODUCTION(), "")
                        .replaceAll("'", "")
                        .split(",");
                _fullWSchedule.add(new scheduleReponse(tempStr[0], tempStr[1], tempStr[9], tempStr[10], tempStr[2], tempStr[5], tempStr[7], tempStr[3], tempStr[6], tempStr[4], tempStr[8], tempStr[11], tempStr[12]));
            }
        }

        return _fullWSchedule;
    }

    public ArrayList<ExamResponse> getExamScheArr() throws IOException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, ParseException {
        WebClient webClient = this.verifyToken();
        WebRequest defaultPage;
        ArrayList<ExamResponse> _fullESchedule = new ArrayList();

        URL actionUrl = new URL(this.definedStr.examSchePageUrl_PRODUCTION());
        defaultPage = new WebRequest(actionUrl);

        defaultPage = this.setAdditionalHeader(defaultPage);

        HtmlPage page = (HtmlPage) webClient
                .getPage(defaultPage);

        DomElement examTable = page.getElementById(definedStr.examTableId_PRODUCTION());
        if(examTable.getTagName().toLowerCase().equals("table")) {
            String _xmlExamSche = this.removeTheDEGap(examTable);
            Matcher examView = this.patternSearch(this.definedStr.examValuPattern_PRODUCTION(), _xmlExamSche);

            int idx = 0;
            String[] incremental = {};
            while (examView.find()) {
                String[] tempStr = examView
                    .group(1)
                    .split("\n");
                incremental = ArrayUtils.addAll(incremental, tempStr[0]);
                idx++;
                if (idx == 9)
                {
                    _fullESchedule.add(new ExamResponse(incremental[0].trim(), incremental[1].trim(), incremental[2].trim(), incremental[3].trim(), incremental[4].trim(), incremental[5].trim(), incremental[6].trim(), incremental[7].trim(), incremental[8].trim()));
                    idx = 0;
                }
            }
        }

        return _fullESchedule;
    }

    public ArrayList<pointResponse> getCurrentPointArr() throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, MalformedURLException {
        WebClient webClient = this.verifyToken();

        URL actionUrl = new URL(this.definedStr.studentPointUrl_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);

        defaultPage = this.setAdditionalHeader(defaultPage);

        HtmlPage page = null;
        try {
            page = (HtmlPage) webClient
                    .getPage(defaultPage);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        DomElement stdntPointTbl = page.getElementById(definedStr.studentCPointTblId_PRODUCTION());

        if(stdntPointTbl.getTagName().toLowerCase().equals("div")) {
            String _xmlPTbl = this.removeTheDEGap(stdntPointTbl);

            return this.showSemesterPointArr(_xmlPTbl);
        }
        return null;
    }

    public String getCurrentPoint() throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, MalformedURLException {
        WebClient webClient = this.verifyToken();

        URL actionUrl = new URL(this.definedStr.studentPointUrl_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);

        defaultPage = this.setAdditionalHeader(defaultPage);

        HtmlPage page = null;
        try {
            page = (HtmlPage) webClient
                    .getPage(defaultPage);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        DomElement stdntPointTbl = page.getElementById(definedStr.studentCPointTblId_PRODUCTION());

        if(stdntPointTbl.getTagName().toLowerCase().equals("div")) {
            String _xmlPTbl = this.removeTheDEGap(stdntPointTbl);

            return this.showSemesterPoint(_xmlPTbl);
        }
        webClient.close();
        return null;
    }

    public String showSemesterPoint(String _xmlPTbl) {
        Gson gson = new Gson();
        return gson.toJson(this.showSemesterPointArr(_xmlPTbl));
    }

    public ArrayList<pointResponse> showSemesterPointArr(String _xmlPTbl) {
        ArrayList<pointResponse> fullPointView = new ArrayList<pointResponse>();
        ArrayList<String> temp = new ArrayList<String>();
        int count = 0;
        Matcher pointView = this.patternSearch(this.definedStr.studentPointPattern_PRODUCTION(), _xmlPTbl);
        while (pointView.find()) {
            temp.add(pointView.group(1).trim());
            if (!!(count == 10))
            {
                fullPointView.add(new pointResponse(temp.get(1), temp.get(2), temp.get(3), temp.get(4), temp.get(5), temp.get(6), temp.get(7), temp.get(8), temp.get(9), temp.get(10)));
                count = -1;
                temp.clear();
            }
            count++;
        }

        return fullPointView;
    }

    public ArrayList<PSListResponse> getPointListSemesterArr() throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, MalformedURLException, UnsupportedEncodingException {
        WebClient webClient = this.verifyToken();
        Matcher pointView = null;

        WebRequest defaultPage = this.viewAllPoint();

        ArrayList<PSListResponse> fullPointView = new ArrayList();

        defaultPage = this.setAdditionalHeader(defaultPage);

        HtmlPage page = null;
        try {
            page = (HtmlPage) webClient
                    .getPage(defaultPage);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        DomElement stdntPointTbl = page.getElementById(definedStr.studentPSemesterId_PRODUCTION());

        String _xmlPTbl = this.removeTheDEGap(stdntPointTbl);
        pointView = this.patternSearch(this.definedStr.studentPListPattern_PRODUCTION(), _xmlPTbl);

        while (pointView.find()) {
            fullPointView.add(new PSListResponse(pointView.group(5).trim(), pointView.group(6).trim()));
        }
        return fullPointView;
    }

    public ArrayList<pointResponse> getPointBySemesterArr(String semesterId) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, MalformedURLException, UnsupportedEncodingException {
        WebClient webClient = this.verifyToken();
        Matcher pointView;
        ArrayList<pointResponse> _empty = new ArrayList<pointResponse>();

        WebRequest defaultPage = this.viewAllPoint();

        defaultPage = this.setAdditionalHeader(defaultPage);

        Matcher firstTest = this.patternSearch(this.definedStr.cboxPointTestPattern_PRODUCTION(), semesterId);
        if (firstTest.find()) {
            semesterId = "Học kỳ " + firstTest.group(1) + " - Năm học " + firstTest.group(2) + "-" + firstTest.group(3);
        }

        HtmlPage page = null;
        try {
            page = (HtmlPage) webClient
                    .getPage(defaultPage);
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }

        DomElement stdntPointTbl = page.getElementById(this.definedStr.studentPSemesterId_PRODUCTION());

        if(stdntPointTbl.getTagName().toLowerCase().equals("div")) {
            String _xmlPTbl = this.removeTheDEGap(stdntPointTbl);

            String myPattern = semesterId.trim().concat(this.definedStr.studentPRangeSelectPattern_PRODUCTION());
            pointView = this.patternSearch(myPattern, _xmlPTbl);

            if (pointView.find())
                return this.showSemesterPointArr(pointView.group(7));
            else return _empty;
        }
        return _empty;
    }

    public String getPointListSemester(String semesterId) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, MalformedURLException, UnsupportedEncodingException {
        Gson gson = new Gson();

        if (semesterId.length() == 0)
            return gson.toJson(this.getPointListSemesterArr());
        else
            return gson.toJson(this.getPointBySemesterArr(semesterId));
    }

    public Matcher patternSearch(String regex, String _test) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(_test);
    }

    public String removeTheDEGap(DomElement theGap) {
        return theGap.asXml().replaceAll("\r\n", "");
    }

    public HtmlPage getScheduleDetailByVS(WebClient wc, String _vs, String _vsg, String ssm, String sw) throws IOException {
        String _acUrl = this.definedStr.schedulePage_PRODUCTION();
        URL actionUrl = new URL(_acUrl);
        WebRequest page = new WebRequest(actionUrl, HttpMethod.POST);
        page = this.setAdditionalHeader(page);

        page.setRequestBody("__EVENTTARGET=ctl00$ContentPlaceHolder1$ctl00$ddlTuan&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=" + URLEncoder.encode(_vs, StandardCharsets.UTF_8.toString()) + "&__VIEWSTATEGENERATOR=" + _vsg + "&ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK=" + URLEncoder.encode(ssm, StandardCharsets.UTF_8.toString()) + "&ctl00$ContentPlaceHolder1$ctl00$ddlLoai=0&ctl00$ContentPlaceHolder1$ctl00$ddlTuan=" + URLEncoder.encode(sw, StandardCharsets.UTF_8.toString()));
        return (HtmlPage) wc
                .getPage(page);
    }

    public WebRequest selectSemesterOpt(String selectedOpt) throws MalformedURLException, UnsupportedEncodingException {
        URL actionUrl = new URL(this.definedStr.schedulePage_PRODUCTION());
        WebRequest schedulePage = new WebRequest(actionUrl, HttpMethod.POST);
        String _VState;
        Object _lock2 = new Object();

        synchronized (_lock2) {
            _VState = this.getVS(this.definedStr.schedulePage_PRODUCTION())[0];
        }

        schedulePage = this.setAdditionalHeader(schedulePage);

        schedulePage.setRequestBody(this.definedStr.requestBodySSO_PRODUCTION()
                .replace("{{VIEWSTATE}}", URLEncoder.encode(_VState, StandardCharsets.UTF_8.toString()))
                .replace("{{CHON_NHHK}}", URLEncoder.encode(selectedOpt, StandardCharsets.UTF_8.toString())));

        return schedulePage;
    }

    public WebRequest selectWeekOpt(String selectedSemester, String selectedWeek) throws MalformedURLException, IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, ParseException, UnsupportedEncodingException {
        Object _lock = new Object();
        Object _lock1 = new Object();
        Object _lock2 = new Object();

        weekResponse weekRes;
        String _acUrl;
        String _VsGet[];

        synchronized (_lock) {
            weekRes = this.findSelectedWeek();
        }

        synchronized (_lock1) {
            _acUrl = weekRes.getTenxacdinh().equals(selectedWeek) ? this.definedStr.schedulePageUrlMode1_PRODUCTION() : this.definedStr.schedulePage_PRODUCTION();
        }


        URL actionUrl = new URL(_acUrl);
        WebRequest schedulePage = new WebRequest(actionUrl, HttpMethod.POST);

        synchronized (_lock2) {
            _VsGet = this.getVS(_acUrl);
        }

        schedulePage.setRequestBody("__EVENTTARGET=ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=" + URLEncoder.encode(_VsGet[0], StandardCharsets.UTF_8.toString()) + "&__VIEWSTATEGENERATOR=" + _VsGet[1] + "&ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK=" + URLEncoder.encode(selectedSemester, StandardCharsets.UTF_8.toString()) + "&ctl00$ContentPlaceHolder1$ctl00$ddlLoai=0");

        return schedulePage;
    }

    public WebRequest viewAllPoint() throws MalformedURLException, UnsupportedEncodingException {
        URL actionUrl = new URL(this.definedStr.studentPointUrl_PRODUCTION());
        WebRequest schedulePage = new WebRequest(actionUrl, HttpMethod.POST);

        schedulePage = this.setAdditionalHeader(schedulePage);

        schedulePage.setRequestBody(this.definedStr.requestBodyVAP_PRODUCTION()
                .replace("{{VIEWSTATE}}", URLEncoder.encode(this.getVS(this.definedStr.studentPointUrl_PRODUCTION())[0], StandardCharsets.UTF_8.toString())));

        return schedulePage;
    }

    public String getExamScheList() throws BadPaddingException, ParseException, NoSuchAlgorithmException, IOException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
        Gson gson = new Gson();

        return gson.toJson(this.getExamScheArr());
    }
}
