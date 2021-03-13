package com.hapi.hapiservice.helpers;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.hapi.hapiservice.models.*;
import com.hapi.hapiservice.services.StudentService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class browserHelper {
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

    //Salt dài hơn không có nghĩa là bảo mật hơn, nhưng 64 là số t thích
    //protected String someSalt = this.encryptEngine.getSalt(64);

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

    public WebClient initial() {
        WebClient webClient = new WebClient();
        webClient.getCache().clear();

        WebClientOptions webClientOpt = webClient.getOptions();
        webClientOpt.setThrowExceptionOnScriptError(false);
        webClientOpt.setThrowExceptionOnFailingStatusCode(false);
        webClientOpt.setJavaScriptEnabled(false);
        webClientOpt.setRedirectEnabled(true);
        //webClientOpt.waitForBackgroundJavaScript(20000);
        webClientOpt.setCssEnabled(false);
        webClientOpt.setTimeout(100000);

        return webClient;
    }

    public String conAuth(boolean isSaved) throws MalformedURLException {
        Gson gson = new Gson();
        WebClient init = this.getLoginVS();
        authSuccess response = new authSuccess(true, "", "");

        if ((this.studentId + "").length() != 10)
            return gson.toJson(new errorResponse(true, "Tài khoản sinh viên không hợp lệ", 404));

        ArrayList<String> studntMP = this.getMailAndPhoneNum();

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
            //Silent is Golden
        } finally {
            return gson.toJson(response);
        }
    }

    public static String generateStdntToken() {
        byte[] randomBytes = new byte[24];
        secureRand.nextBytes(randomBytes);
        return base64.encodeToString(randomBytes);
    }

    public WebClient getLoginVS () throws MalformedURLException {
        URL actionUrl = new URL(this.definedStr.defaultPage_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);
        String parseBody;//, _viewState = null, _cookies = null;

        defaultPage.setAdditionalHeader("User-Agent", this.definedStr.userAgentDefault_PRODUCTION());

        try {
            HtmlPage page = (HtmlPage) this.webClient
                    .getPage(defaultPage);

            HtmlInput logInput = page.getHtmlElementById(this.definedStr.loginId_PRODUCTION());
            HtmlInput pwdInput = page.getHtmlElementById(this.definedStr.loginPwdId_PRODUCTION());

            logInput.setValueAttribute(this.studentId + "");
            pwdInput.setValueAttribute(this.studentPassword);

            page = page.getHtmlElementById(this.definedStr.loginBtnId_PRODUCTION()).click();

            //Thread.sleep(1500);

            parseBody = page.asXml();

            Pattern pattern = Pattern.compile(this.definedStr.invalidCredentialsPattern_PRODUCTION());
            Matcher credentialsChecker = pattern.matcher(parseBody);
            if (credentialsChecker.find()) {
                webClient.close();
                return null;
            }

            //CookieManager cookieManager = webClient.getCookieManager();
            //_cookies = cookieManager.getCookies().toString();
        } catch (Exception e) {
            //Silent is Golden
        } finally {
            return webClient;
        }
    }

    public String getVS(String initialUrl) throws MalformedURLException {
        URL actionUrl = new URL(initialUrl);
        WebRequest defaultPage = new WebRequest(actionUrl);
        String _viewState = null;

        try {
            HtmlPage page = (HtmlPage) this.webClient
                    .getPage(defaultPage);

            _viewState = page.getElementById("__VIEWSTATE").getAttribute("value");
        } catch (Exception e) {
            //Silent is Golden
        } finally {
            return _viewState;
        }
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
            //Silent is Golden
        } finally {
            return _studntName;
        }
    }

    public ArrayList<String> getMailAndPhoneNum() throws MalformedURLException {
        URL actionUrl = new URL(this.definedStr.userInfoChangerUrl_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);
        ArrayList<String> _infoBack = new ArrayList<String>();

        try {
            HtmlPage page = (HtmlPage) this.webClient
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
            //Silent is Golden
        } finally {
            return _infoBack;
        }
    }

    public WebClient verifyToken() throws MalformedURLException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Students tokenInfo = this.studentService.getStudentByToken(this.token);

        this.studentId = tokenInfo.getSid();
        this.studentPassword = this.encryptEngine.pleaseHelpViHackThis(tokenInfo.getPwd());

        return this.getLoginVS();
    }

    public String getSemesterList() throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        WebClient webClient = this.verifyToken();

        URL actionUrl = new URL(this.definedStr.schedulePage_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);
        String[] _semesters = new String[] {};
        ArrayList<semesterResponse> semesterMap = new ArrayList<>();

        Gson gson = new Gson();

        defaultPage.setAdditionalHeader("User-Agent", this.definedStr.userAgentDefault_PRODUCTION());

        try {
            HtmlPage page = (HtmlPage) webClient
                    .getPage(defaultPage);

            DomElement semesterOpt = page.getElementById(definedStr.semesterOptId_PRODUCTION());

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

        } catch (Exception e) {
            //Silent is Golden
        } finally {
            webClient.close();
            return gson.toJson(semesterMap);
        }
    }

    public String getWeekList(String currSemester) throws MalformedURLException, InterruptedException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        WebClient webClient = this.verifyToken();
        WebRequest defaultPage;

        if (currSemester != null)
            defaultPage = this.selectSemesterOpt(currSemester);
        else {
            URL actionUrl = new URL(this.definedStr.schedulePage_PRODUCTION());
            defaultPage = new WebRequest(actionUrl);
        }

        ArrayList<weekResponse> allSWeek = new ArrayList<>();

        Gson gson = new Gson();

        defaultPage.setAdditionalHeader("User-Agent", this.definedStr.userAgentDefault_PRODUCTION());

        try {
            HtmlPage page = (HtmlPage) webClient
                    .getPage(defaultPage);

            DomElement semesterOpt = page.getElementById(definedStr.weekOptId_PRODUCTION());

            if(semesterOpt.getTagName().toLowerCase().equals("select")) {
                String _xmlWeek = this.removeTheDEGap(semesterOpt);
                Matcher weekView = this.patternSearch(this.definedStr.miniOptPattern_PRODUCTION(), _xmlWeek);
                if (!weekView.find())
                    return null;
                else
                    while (weekView.find()) {
                        String tempStr = weekView.group(1);
                        Matcher weekNum = this.patternSearch(this.definedStr.weekNumOptPattern_PRODUCTION(), tempStr);
                        Matcher startDate = this.patternSearch(this.definedStr.startDateOptPattern_PRODUCTION(), tempStr);
                        Matcher endDate = this.patternSearch(this.definedStr.endDateOptPattern_PRODUCTION(), tempStr);
                        if (weekNum.find() && startDate.find() && endDate.find())
                            allSWeek.add(new weekResponse(startDate.group(1), endDate.group(1), weekNum.group(1), tempStr));
                    }
            }
        } catch (Exception e) {
            //Silent is Golden
        } finally {
            webClient.close();
            return gson.toJson(allSWeek);
        }
    }

    public String getScheduleDetail(String currSemester, String currWeek) throws MalformedURLException, InterruptedException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        WebClient webClient = this.verifyToken();
        WebRequest defaultPage;

        if (currSemester != null && currWeek != null)
            defaultPage = this.selectWeekOpt(currSemester, currWeek);
        else {
            URL actionUrl = new URL(this.definedStr.schedulePage_PRODUCTION());
            defaultPage = new WebRequest(actionUrl);
        }

        ArrayList<scheduleReponse> fullWSchedule = new ArrayList<>();
        Gson gson = new Gson();

        defaultPage.setAdditionalHeader("User-Agent", this.definedStr.userAgentDefault_PRODUCTION());

        try {
            HtmlPage page = (HtmlPage) webClient
                    .getPage(defaultPage);

            DomElement scheduleTable = page.getElementById(definedStr.scheduleTableId_PRODUCTION());

            if(scheduleTable.getTagName().toLowerCase().equals("table")) {
                String _xmlSchedule = this.removeTheDEGap(scheduleTable);
                Matcher scheduleView = this.patternSearch(this.definedStr.schedulePattern_PRODUCTION(), _xmlSchedule);
                if (!scheduleView.find())
                    return null;
                else
                    while (scheduleView.find()) {
                        String[] tempStr = scheduleView
                                .group(1)
                                .replaceAll(this.definedStr.scheduleDescRemove_PRODUCTION(), "")
                                .replaceAll("'", "")
                                .split(",");
                        fullWSchedule.add(new scheduleReponse(tempStr[0], tempStr[1], tempStr[9], tempStr[10], tempStr[2], tempStr[5], tempStr[7], tempStr[3], tempStr[6], tempStr[4], tempStr[8], tempStr[11], tempStr[12]));
                    }
            }
        } catch (Exception e) {
            //Silent is Golden
        } finally {
            webClient.close();
            return gson.toJson(fullWSchedule);
        }
    }

    public String getCurrentPoint() throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, MalformedURLException {
        WebClient webClient = this.verifyToken();

        URL actionUrl = new URL(this.definedStr.studentPointUrl_PRODUCTION());
        WebRequest defaultPage = new WebRequest(actionUrl);
        String _result = "";

        defaultPage.setAdditionalHeader("User-Agent", this.definedStr.userAgentDefault_PRODUCTION());

        try {
            HtmlPage page = (HtmlPage) webClient
                    .getPage(defaultPage);

            DomElement stdntPointTbl = page.getElementById(definedStr.studentCPointTblId_PRODUCTION());

            if(stdntPointTbl.getTagName().toLowerCase().equals("div")) {
                String _xmlPTbl = this.removeTheDEGap(stdntPointTbl);

                _result = this.showSemesterPoint(_xmlPTbl);
            }
        } catch (Exception e) {
            //Silent is Golden
        } finally {
            webClient.close();
            return _result;
        }
    }

    public String showSemesterPoint(String _xmlPTbl) {
        ArrayList<pointResponse> fullPointView = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        int count = 0;
        Gson gson = new Gson();
        Matcher pointView = this.patternSearch(this.definedStr.studentPointPattern_PRODUCTION(), _xmlPTbl);

        if (!pointView.find())
            return null;
        else
            while (pointView.find()) {
                temp.add(pointView.group(1).trim());
                if (!!(count == 10))
                {
                    fullPointView.add(new pointResponse(temp.get(0),temp.get(1), temp.get(2), temp.get(3), temp.get(4), temp.get(5), temp.get(6), temp.get(7), temp.get(8), temp.get(9)));
                    count = -1;
                    temp.clear();
                }
                count++;
            }
        return gson.toJson(fullPointView);
    }

    public String getPointListSemester(String semesterId) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, MalformedURLException {
        WebClient webClient = this.verifyToken();
        Matcher pointView;

        WebRequest defaultPage = this.viewAllPoint();
        String _last = null;

        ArrayList<PSListResponse> fullPointView = new ArrayList<>();
        Gson gson = new Gson();

        defaultPage.setAdditionalHeader("User-Agent", this.definedStr.userAgentDefault_PRODUCTION());

        try {
            HtmlPage page = (HtmlPage) webClient
                    .getPage(defaultPage);

            DomElement stdntPointTbl = page.getElementById(definedStr.studentPSemesterId_PRODUCTION());

            if(stdntPointTbl.getTagName().toLowerCase().equals("div")) {
                String _xmlPTbl = this.removeTheDEGap(stdntPointTbl);
                if (semesterId == "")
                    pointView = this.patternSearch(this.definedStr.studentPListPattern_PRODUCTION(), _xmlPTbl);
                else {
                    String myPattern = semesterId.trim().concat(this.definedStr.studentPRangeSelectPattern_PRODUCTION());
                    pointView = this.patternSearch(myPattern, _xmlPTbl);
                }

                if (!pointView.find())
                    return null;
                else if (semesterId == "")
                {
                    while (pointView.find()) {
                        fullPointView.add(new PSListResponse(pointView.group(5).trim()));
                    }
                    _last = gson.toJson(fullPointView);
                }
                else {
                    _last = this.showSemesterPoint(pointView.group(7));
                }
            }
        } catch (Exception e) {
            //Silent is Golden
        } finally {
            webClient.close();
            return _last;
        }
    }

    public Matcher patternSearch(String regex, String _test) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(_test);
    }

    public String removeTheDEGap(DomElement theGap) {
        return theGap.asXml().replaceAll("\r\n", "");
    }

    public WebRequest selectSemesterOpt(String selectedOpt) throws MalformedURLException {
        URL actionUrl = new URL(this.definedStr.schedulePage_PRODUCTION());
        WebRequest schedulePage = new WebRequest(actionUrl, HttpMethod.POST);

        schedulePage.setAdditionalHeader("User-Agent", this.definedStr.userAgentDefault_PRODUCTION());

        schedulePage.setRequestBody("__EVENTTARGET=ctl00\\$ContentPlaceHolder1\\$ctl00\\$ddlChonNHHK&__EVENTARGUMENT=&__LASTFOCUS=&ctl00$ContentPlaceHolder1$ctl00$ddlLoai=0&__VIEWSTATE=" + URLEncoder.encode(this.getVS(this.definedStr.schedulePage_PRODUCTION())) + "&ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK=" + URLEncoder.encode(selectedOpt) + "&ctl00$ContentPlaceHolder1$ctl00$rad_MonHoc=rad_MonHoc");

        return schedulePage;
    }

    public WebRequest selectWeekOpt(String selectedSemester, String selectedWeek) throws MalformedURLException {
        URL actionUrl = new URL(this.definedStr.schedulePage_PRODUCTION());
        WebRequest schedulePage = new WebRequest(actionUrl, HttpMethod.POST);

        schedulePage.setAdditionalHeader("User-Agent", this.definedStr.userAgentDefault_PRODUCTION());

        schedulePage.setRequestBody("__EVENTTARGET=ctl00$ContentPlaceHolder1$ctl00$ddlTuan&__EVENTARGUMENT=&__LASTFOCUS=&ctl00$ContentPlaceHolder1$ctl00$ddlLoai=0&__VIEWSTATE=" + URLEncoder.encode(this.getVS(this.definedStr.schedulePage_PRODUCTION())) + "&ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK=" + URLEncoder.encode(selectedSemester) + "&ctl00$ContentPlaceHolder1$ctl00$ddlLoai=0&ctl00$ContentPlaceHolder1$ctl00$ddlTuan=" + URLEncoder.encode(selectedWeek));

        return schedulePage;
    }

    public WebRequest viewAllPoint() throws MalformedURLException {
        URL actionUrl = new URL(this.definedStr.studentPointUrl_PRODUCTION());
        WebRequest schedulePage = new WebRequest(actionUrl, HttpMethod.POST);

        schedulePage.setAdditionalHeader("User-Agent", this.definedStr.userAgentDefault_PRODUCTION());

        schedulePage.setRequestBody("__EVENTTARGET=ctl00$ContentPlaceHolder1$ctl00$lnkChangeview2&__EVENTARGUMENT=&__LASTFOCUS=&ctl00$ContentPlaceHolder1$ctl00$txtChonHK=&__VIEWSTATE=" + URLEncoder.encode(this.getVS(this.definedStr.studentPointUrl_PRODUCTION())));

        return schedulePage;
    }
}
