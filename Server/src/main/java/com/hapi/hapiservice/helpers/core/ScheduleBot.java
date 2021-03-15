package com.hapi.hapiservice.helpers.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hapi.hapiservice.helpers.common.browserHelper;
import com.hapi.hapiservice.helpers.common.stringHelper;
import com.hapi.hapiservice.helpers.respository.ChatbotStudentRepository;
import com.hapi.hapiservice.helpers.respository.StudentRepository;
import com.hapi.hapiservice.models.bot.*;
import com.hapi.hapiservice.models.schedule.Students;
import com.hapi.hapiservice.models.schedule.scheduleReponse;
import com.hapi.hapiservice.models.schedule.semesterResponse;
import com.hapi.hapiservice.models.schedule.weekResponse;
import com.hapi.hapiservice.services.ChatbotStudentService;
import com.hapi.hapiservice.services.StudentService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduleBot {
    final stringHelper definedStr = new stringHelper();
    private ChatbotStudentRepository chatbotStudentRepository;
    private ChatbotStudentService chatbotStudentService;
    private String fid;
    private StudentRepository studentRepository;
    private StudentService studentService;
    private Object _lock = new Object();

    public ScheduleBot(
            String fid,
            ChatbotStudentRepository chatbotStudentRepository,
            ChatbotStudentService chatbotStudentService,
            StudentRepository studentRepository,
            StudentService studentService) {
        this.fid = fid;
        this.chatbotStudentRepository = chatbotStudentRepository;
        this.chatbotStudentService = chatbotStudentService;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    public Message onMessageCome (String message) {
        String reply = "Bạn nói vậy mình không hiểu :3 nếu bạn muốn có tính năng mới hãy liên hệ quản lý của mình qua email mail.cua.ql.hapi.ne@roleplay[.]vn (nhớ bỏ dấu chấm '.' giữa roleplay và vn)";
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại"),
                new Button().setContentType("text").setTitle("Báo lỗi").setPayload("Báo lỗi")
        };

        /*if (this.patternSearch(definedStr.cbotLogin_PRODUCTION(), message)) {
            return this.login("");
        } else if(this.patternSearch(definedStr.cboxStartFirstInitialPattern_PRODUCTION(), message)) {
            return this.startFirstInitial();
        } else if(this.patternSearch(definedStr.cboxExtractSemesterPattern_PRODUCTION(), message)) {
            return this.startWeekInitial(message);
        }*/

        return new Message().setText(reply).setQuickReplies(buttons);
    }

    public int getStudentIdByFid() {
        Optional<ChatbotStudents> testAuth = this.chatbotStudentService.findById(this.fid);
        if (testAuth.isPresent() && testAuth.get().getSid() != 0)
            return testAuth.get().getSid();
        else return -1;
    }

    public Message startFirstInitial() throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, MalformedURLException {
        if (this.getStudentIdByFid() == -1) {
            return this.login("Bạn chưa đăng nhập hoặc mật khẩu của bạn đã bị thay đổi! \nNhấp vào nút *\"Lưu tài khoản\"* để lưu tài khoản bạn nhé!\nSau khi đã lưu xong bạn hãy gõ *Bắt đầu* hoặc nhấp nút *Bắt đầu lại*");
        }
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại"),
                new Button().setContentType("text").setTitle("Báo lỗi").setPayload("Báo lỗi")
        };
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        String reply = "Hãy chọn học kỳ bạn muốn xem thời khóa biểu";

        if (studentCre.isPresent()) {
            browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);
            ArrayList<Button> listBtn = new ArrayList<Button>();

            for(semesterResponse semester : studentBasicTest.getSemesterArr()) {
                String currentId = "Học kỳ " + semester.semesterId.charAt(semester.semesterId.length() - 1) + ", " +  semester.semesterId.trim().substring(0, 4);
                listBtn.add(new Button().setContentType("text").setTitle(currentId).setPayload(currentId));
            };

            return new Message().setText(reply).setQuickReplies(this.ArrToBtnArr(listBtn));
        } else {
            return new Message().setText(this.definedStr.pleaseTryAgainlab_PRODUCTION()).setQuickReplies(buttons);
        }
    }

    public Message startWeekInitial(String semesterId) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, MalformedURLException {
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        String reply = "Hãy gõ tuần bạn muốn xem thời khóa biểu và gửi lại cho Hapi";
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại"),
                new Button().setContentType("text").setTitle("Báo lỗi").setPayload("Báo lỗi")
        };
        browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);

        Matcher extractSemesterId = this.patternSearchAndGet(this.definedStr.cboxExtractSemesterPattern_PRODUCTION(), semesterId);

        if (studentCre.isPresent() && extractSemesterId.find()) {
            ChatbotStudents cbst = this.chatbotStudentService.findById(this.fid).get();

            semesterId = extractSemesterId.group(2) + extractSemesterId.group(1);

            cbst.setCurrentSemesterId(semesterId);
            this.chatbotStudentService.save(cbst);

            ArrayList<Button> listBtn = new ArrayList<Button>();

            if (studentBasicTest.getWeekArr(semesterId).size() != 0)
                for(weekResponse week : studentBasicTest.getWeekArr(semesterId)) {
                    //listBtn.add(new Button().setContentType("text").setTitle(week.ngaybd + " - " + week.ngaykt + " (Tuần " + week.sotuan + ")").setPayload(week.unixtimebd + "x" +  week.unixtimekt));
                    reply += "\nTuần " + week.sotuan + " (từ ngày " + week.ngaybd + " đến ngày " + week.ngaykt + ")";
                }
            else reply = "Bạn không có thời khóa biểu trong học kỳ này!";

            reply += "\nFacebook không cho phép có quá nhiều nút bấm trong 1 lần, các bạn thông cảm giúp Hapi nha <3!" +
            "\nVí dụ bạn muốn xem thời khóa biểu tuần 26, hãy nhập: 26";

            return new Message().setText(reply).setQuickReplies(buttons);
        }
        return new Message().setText(this.definedStr.pleaseTryAgainlab_PRODUCTION()).setQuickReplies(buttons);
    }

    private String getWeekByWeekNum(String weekNum) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, MalformedURLException {
        ChatbotStudents cbst = this.chatbotStudentService.findById(this.fid).get();
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);

        for(weekResponse week : studentBasicTest.getWeekArr(cbst.getCurrentSemesterId())) {
            if (Integer.parseInt(week.sotuan) == Integer.parseInt(weekNum))
                return week.tenxacdinh;
        }

        return "";
    }

    public Message getScheduleByWeekAndSemester(String weekId) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, InterruptedException {
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại"),
                new Button().setContentType("text").setTitle("Báo lỗi").setPayload("Báo lỗi")
        };
        String reply = "Sau đây là thông tin thời khóa biểu tuần " + weekId + " của bạn:\n--------------------------------";

        if (studentCre.isPresent()) {
            ChatbotStudents cbst = this.chatbotStudentService.findById(this.fid).get();
            String semesterId = cbst.getCurrentSemesterId();

            //weekId = "Tuần " + extractWeekId.group(1) + " [Từ " + extractWeekId.group(2) + " -- " + extractWeekId.group(3) + "]";
            weekId = this.getWeekByWeekNum(weekId);

            if (weekId == "")
                return new Message().setText(this.definedStr.pleaseTryAgainlab_PRODUCTION()).setQuickReplies(buttons);

            cbst.setCurrentWeekId(weekId);
            this.chatbotStudentService.save(cbst);

            browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);

            synchronized(_lock) {
                ArrayList<scheduleReponse> stdntList = studentBasicTest.getScheduleDetailArr(semesterId, weekId);

                if (stdntList != null && stdntList.size() != 0)
                    for (scheduleReponse scdule : stdntList) {
                        reply += "\n\n*" + scdule.thu + "* tiết *" + scdule.tietbatdau + "*: học *" + scdule.sotiet + " tiết* môn *" + scdule.mon + "* (" + scdule.tinchi + " tín chỉ) với, lớp *" + scdule.lop + "* (" + scdule.nhom + ") tại phòng *" + scdule.phong + "*" + (scdule.giangvien.length() > 5 ? " do giảng viên " + scdule.giangvien + " dạy" : "") + ". Bạn sẽ học môn này từ ngày " + scdule.ngaybd + " đến ngày " + scdule.ngaykt + ".";
                    }
                else reply = "Bạn không có thời khóa biểu trong tuần này!";
            }

            return new Message().setText(reply).setQuickReplies(buttons);
        }
        return new Message().setText(this.definedStr.pleaseTryAgainlab_PRODUCTION()).setQuickReplies(buttons);
    }

    private String encryptFid (String str) {
        String encoded = new String(Base64.getEncoder().encode(str.getBytes()));
        return encoded;
    }

    public Message login (String changeMyMind) {
        String reply = changeMyMind == "" ? "Nhấp vào nút *\"Lưu tài khoản\"* để lưu tài khoản bạn nhé!\nSau khi đã lưu xong bạn hãy gõ *Bắt đầu* hoặc nhấp nút *Bắt đầu lại*" : changeMyMind;
        Button[] buttons = new Button[]{
                new Button().setType("web_url").setUrl("https://h.2cll.com?fid=" + this.encryptFid(this.fid)).setTitle("Lưu tài khoản")
        };

        Button[] buttons2 = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại")
        };

        return new Message().setAttachment(new Attachment().setType("template").setPayload(new Payload()
                .setTemplateType("button").setText(reply).setButtons(buttons))).setQuickReplies(buttons2);
    }

    public Message userConfigRequest () {
        if (this.getStudentIdByFid() == -1) {
            return this.login(this.definedStr.cboxInvalidCredentialsPlsLogAgain_PRODUCTION());
        }

        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại"),
                new Button().setContentType("text").setTitle("Đăng xuất").setPayload("Đăng xuất")
        };
        String reply = "Hãy chọn hành động bạn muốn, bắt đầu lại hoặc đăng xuất tùy ý huynh ^^";

        return new Message().setText(reply).setQuickReplies(buttons);
    }

    public Message showPointOpt() {
        if (this.getStudentIdByFid() == -1) {
            return this.login(this.definedStr.cboxInvalidCredentialsPlsLogAgain_PRODUCTION());
        }

        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("Điểm hiện tại").setPayload("Điểm hiện tại"),
                new Button().setContentType("text").setTitle("Tất cả điểm").setPayload("Tất cả điểm")
        };
        String reply = "Hãy chọn không gian bạn muốn, *điểm hiện tại* tức là điểm của học kỳ hiện tại, *tất cả điểm* tức là điểm của tất cả học kỳ ^^";

        return new Message().setText(reply).setQuickReplies(buttons);
    }

    public Message detailPointOpt(String msg) {
        if (this.getStudentIdByFid() == -1) {
            return this.login(this.definedStr.cboxInvalidCredentialsPlsLogAgain_PRODUCTION());
        }

        if (msg.toLowerCase().trim() == "điểm hiện tại")
        {
            Button[] buttons = new Button[]{
                    new Button().setContentType("text").setTitle("Điểm hiện tại").setPayload("Điểm hiện tại"),
                    new Button().setContentType("text").setTitle("Tất cả điểm").setPayload("Tất cả điểm")
            };
            String reply = "Hãy chọn không gian bạn muốn, *điểm hiện tại* tức là điểm của học kỳ hiện tại, *tất cả điểm* tức là điểm của tất cả học kỳ ^^";
            return new Message().setText(reply).setQuickReplies(buttons);
        } else {
            Button[] buttons = new Button[]{
                    new Button().setContentType("text").setTitle("Điểm hiện tại").setPayload("Điểm hiện tại"),
                    new Button().setContentType("text").setTitle("Tất cả điểm").setPayload("Tất cả điểm")
            };
            String reply = "Hãy chọn không gian bạn muốn, *điểm hiện tại* tức là điểm của học kỳ hiện tại, *tất cả điểm* tức là điểm của tất cả học kỳ ^^";
            return new Message().setText(reply).setQuickReplies(buttons);
        }
    }

    public Message userConfigCome () {
        if (this.getStudentIdByFid() == -1) {
            return this.login(this.definedStr.cboxInvalidCredentialsPlsLogAgain_PRODUCTION());
        }

        this.chatbotStudentService.findById(this.fid).ifPresent(cbstdnt -> {
            cbstdnt.setSid(0);
            this.chatbotStudentService.save(cbstdnt);
        });
        
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại")
        };
        String reply = "Đăng xuất tài khoản thành công khỏi hệ thống Hapi. Cảm ơn bạn đã sử dụng dịch vụ!\nHãy giới thiệu bạn bè học chung tại HUTECH để họ có thể biết về dịch vụ miễn phí này nhè <3 <3";

        return new Message().setText(reply).setQuickReplies(buttons);
    }

    public void saveFbCredentials (int sid) {
        ChatbotStudents cbfb = new ChatbotStudents();
        cbfb.setFid(this.fid);
        cbfb.setSid(sid);
        this.chatbotStudentService.save(cbfb);
    }

    public boolean patternSearch(String regex, String _test) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(_test).find();
    }

    public Matcher patternSearchAndGet(String regex, String _test) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(_test);
    }

    public Button[] ArrToBtnArr (ArrayList<Button> buttons) {
        Button[] objs = new Button[buttons.size()];
        for(int i= 0; i < buttons.size(); i++)
            objs[i] = buttons.get(i);

        return objs;
    }
}
