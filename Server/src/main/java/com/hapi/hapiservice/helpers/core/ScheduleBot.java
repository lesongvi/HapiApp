package com.hapi.hapiservice.helpers.core;

import com.hapi.hapiservice.helpers.common.browserHelper;
import com.hapi.hapiservice.helpers.common.snapshotHelper;
import com.hapi.hapiservice.helpers.common.stringHelper;
import com.hapi.hapiservice.helpers.respository.ChatbotStudentRepository;
import com.hapi.hapiservice.helpers.respository.NotificationRespository;
import com.hapi.hapiservice.helpers.respository.StudentRepository;
import com.hapi.hapiservice.models.bot.*;
import com.hapi.hapiservice.models.schedule.*;
import com.hapi.hapiservice.services.ChatbotStudentService;
import com.hapi.hapiservice.services.NotificationService;
import com.hapi.hapiservice.services.StudentService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduleBot {
    final stringHelper definedStr = new stringHelper();
    private ChatbotStudentRepository chatbotStudentRepository;
    private ChatbotStudentService chatbotStudentService;
    private String fid;
    private StudentRepository studentRepository;
    private StudentService studentService;
    private final Object _lock = new Object();
    Logger logger = Logger.getLogger(ScheduleBot.class.getName());

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

        return new Message().setText(reply).setQuickReplies(buttons);
    }

    public Message NotificationLookup(NotificationRespository notificationRespository, NotificationService notificationService) throws IOException {
        snapshotHelper snapshot = new snapshotHelper(notificationRespository, notificationService);
        String reply = snapshot.snapshotNotificationNotify().getNotification();
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại"),
                new Button().setContentType("text").setTitle("Báo lỗi").setPayload("Báo lỗi")
        };

        return new Message().setText(reply).setQuickReplies(buttons);
    }

    public Message currentWeekView() throws BadPaddingException, InterruptedException, ParseException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        if (this.getStudentIdByFid() == -1) {
            return this.login("Bạn chưa đăng nhập hoặc mật khẩu của bạn đã bị thay đổi! \nNhấp vào nút *\"Lưu tài khoản\"* để lưu tài khoản bạn nhé!\nSau khi đã lưu xong bạn hãy gõ *Bắt đầu* hoặc nhấp nút *Bắt đầu lại*");
        }

        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại"),
                new Button().setContentType("text").setTitle("Báo lỗi").setPayload("Báo lỗi")
        };
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());

        if (studentCre.isPresent()) {
            browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);

            weekResponse selectedWeek;
            synchronized (_lock) {
                selectedWeek = studentBasicTest.findSelectedWeek();
            }

            return this.getScheduleByWeekAndSemester(selectedWeek.getSotuan());
        }
        return new Message().setText(this.definedStr.pleaseTryAgainlab_PRODUCTION()).setQuickReplies(buttons);
    }

    public int getStudentIdByFid() {
        Optional<ChatbotStudents> testAuth = this.chatbotStudentService.findById(this.fid);
        if (testAuth.isPresent() && testAuth.get().getSid() != 0)
            return testAuth.get().getSid();
        else return -1;
    }

    public Message startFirstInitial() throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException {
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

            /*if (studentBasicTest.isServerReloading()) {
                return new Message().setText(this.definedStr.serverReloadingPlsTryAgainLab_PRODUCTION());
            }*/

            listBtn.add(new Button().setContentType("text").setTitle("TKB tuần này").setPayload("TKB tuần này"));

            for(semesterResponse semester : studentBasicTest.getSemesterArr()) {
                String currentId = "Học kỳ " + semester.semesterId.charAt(semester.semesterId.length() - 1) + ", " +  semester.semesterId.trim().substring(0, 4);
                listBtn.add(new Button().setContentType("text").setTitle(currentId).setPayload(currentId));
            };

            return new Message().setText(reply).setQuickReplies(this.ArrToBtnArr(listBtn));
        } else {
            return new Message().setText(this.definedStr.pleaseTryAgainlab_PRODUCTION()).setQuickReplies(buttons);
        }
    }

    //https://github.com/lesongvi/Personal-Schedule/blob/f547554eeb9537c3760e3c4e161b5cf1d2797f9c/QUANLYTHOIGIANHUTECH/ViewModels/dataStudent.cs
    public String convertTime(int tiet)
    {
        switch (tiet)
        {
            case 1:
                return "06:45 AM";
            case 2:
                return "07:30 AM";
            case 3:
                return "08:15 AM";
            case 4:
                return "09:20 AM";
            case 5:
                return "10:05 AM";
            case 6:
                return "10:50 AM";
            case 7:
                return "12:30 PM";
            case 8:
                return "01:15 PM";
            case 9:
                return "02:00 PM";
            case 10:
                return "03:05 PM";
            case 11:
                return "03:50 PM";
            case 12:
                return "04:35 PM";
            case 13:
                return "06:00 PM";
            case 14:
                return "06:45 PM";
            case 15:
                return "07:30 PM";
            default:
                return null;
        }
    }

    public Message startWeekInitial(String semesterId) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, MalformedURLException, InterruptedException {
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        String reply = "Hãy gõ *số tuần* bạn muốn xem thời khóa biểu và nhấp Enter\n-------------------";
        ArrayList<weekResponse> weekArr;
        String _randomExample = "Tuần 19";
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại"),
                new Button().setContentType("text").setTitle("TKB tuần này").setPayload("TKB tuần này"),
                new Button().setContentType("text").setTitle("Xem thời khóa biểu").setPayload("Xem thời khóa biểu"),
                new Button().setContentType("text").setTitle("Báo lỗi").setPayload("Báo lỗi")
        };
        browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);

        Matcher extractSemesterId = this.patternSearchAndGet(this.definedStr.cboxExtractSemesterPattern_PRODUCTION(), semesterId);

        if (studentCre.isPresent() && extractSemesterId.find()) {
            ChatbotStudents cbst = this.chatbotStudentService.findById(this.fid).get();

            semesterId = extractSemesterId.group(2) + extractSemesterId.group(1);

            synchronized (_lock) {
                weekArr = studentBasicTest.getWeekArr(semesterId);
            }

            cbst.setCurrentSemesterId(semesterId);
            this.chatbotStudentService.save(cbst);

            if (weekArr.size() != 0)
            {
                for(weekResponse week : weekArr) {
                    reply += "\nTuần *" + week.getSotuan() + "* (từ ngày *" + week.getNgaybd() + "* đến ngày *" + week.getNgaykt() + "*)";
                }
                reply += //"\nFacebook không cho phép có quá nhiều nút bấm trong 1 lần, các bạn thông cảm giúp Hapi nha <3!" +
                        "\n\nVí dụ bạn muốn xem thời khóa biểu " + weekArr.get(0).getSotuan() + " (từ ngày " + weekArr.get(0).getNgaybd() + " - " + weekArr.get(0).getNgaykt() + ", hãy nhập: 26";
            }
            else reply = "Bạn không có thời khóa biểu trong học kỳ này!";


            return new Message().setText(reply).setQuickReplies(buttons);
        }
        return new Message().setText(this.definedStr.pleaseTryAgainlab_PRODUCTION()).setQuickReplies(buttons);
    }

    private String getWeekByWeekNum(String weekNum) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, MalformedURLException {
        ChatbotStudents cbst = this.chatbotStudentService.findById(this.fid).get();
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);

        for(weekResponse week : studentBasicTest.getWeekArr(cbst.getCurrentSemesterId())) {
            if (Integer.parseInt(week.getSotuan()) == Integer.parseInt(weekNum))
                return week.getTenxacdinh();
        }

        return "";
    }

    public Message getScheduleByWeekAndSemester(String weekId) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, InterruptedException, ParseException {
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại"),
                new Button().setContentType("text").setTitle("Xem thời khóa biểu").setPayload("Xem thời khóa biểu"),
                new Button().setContentType("text").setTitle("TKB tuần này").setPayload("TKB tuần này"),
                new Button().setContentType("text").setTitle("Báo lỗi").setPayload("Báo lỗi")
        };
        String reply = "Sau đây là thông tin thời khóa biểu tuần " + weekId + " của bạn:\n--------------------------------";

        if (studentCre.isPresent()) {
            ChatbotStudents cbst = this.chatbotStudentService.findById(this.fid).get();
            String semesterId = cbst.getCurrentSemesterId();

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
                        reply += "\n\n*" + scdule.thu + "* tiết *" + scdule.tietbatdau + "* (" + this.convertTime(Integer.parseInt(scdule.tietbatdau)) + "): học *" + scdule.sotiet + " tiết* môn *" + scdule.mon + "* (" + scdule.tinchi + " tín chỉ) với, lớp *" + scdule.lop + "* (" + scdule.nhom + ") tại phòng *" + scdule.phong + "*" + (scdule.giangvien.length() > 5 ? " do giảng viên " + scdule.giangvien + " dạy" : "") + ". Bạn sẽ học môn này từ ngày " + scdule.ngaybd + " đến ngày " + scdule.ngaykt + ".";
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

    public Message detailPointOpt(String msg) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, IOException {
        if (this.getStudentIdByFid() == -1) {
            return this.login(this.definedStr.cboxInvalidCredentialsPlsLogAgain_PRODUCTION());
        }
        double diemHocjKyHe4 = 0, tinChiHocKy = 0;
        ArrayList<pointResponse> pointFullList;

        ArrayList<Button> listBtn = new ArrayList<>();
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);


        /*if (studentBasicTest.isServerReloading()) {
            return new Message().setText(this.definedStr.serverReloadingPlsTryAgainLab_PRODUCTION());
        }*/

        if (msg.toLowerCase().trim().equals("điểm hiện tại"))
        {
            String reply = "Sau đây là danh sách điểm của bạn\n-----------------------------";
            Button[] buttons = new Button[]{
                    new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại"),
                    new Button().setContentType("text").setTitle("Báo lỗi").setPayload("Báo lỗi")
            };
            synchronized (this._lock) {
                pointFullList = studentBasicTest.getCurrentPointArr();

                if (pointFullList.size() != 0)
                {
                    for (pointResponse point : pointFullList) {
                        reply += "\n\nMôn " + point.getTenmon() + " (" + point.getTinchi() + " tín chỉ): điểm kiểm tra lần 1 *" + point.getDiemkt1() + "*, điểm kiểm tra lần 2 *" + point.getDiemkt2() + "*, điểm thi *" + point.getThil1() + "*, điểm tổng (thang điểm 4): *" + point.getTk4() + "* (Đạt loại *" + point.getTkch() + "*)";
                        if (this.isNumeric(point.getTk4()) && this.isNumeric(point.getTinchi()))
                        {
                            diemHocjKyHe4 += Double.parseDouble(point.getTk4());
                            tinChiHocKy += Double.parseDouble(point.getTinchi());
                        }
                    }
                    diemHocjKyHe4 /= pointFullList.size();
                    reply += "\n\nTổng kết:\n- Điểm trung bình học kỳ (thang 4): *" + diemHocjKyHe4 + "* (Xếp loại: " + this.rankByPoint(diemHocjKyHe4) + ")\n- Số tín chỉ đạt được trong học kỳ: " + tinChiHocKy ;
                }
                else reply = "Bạn chưa có điểm nào cả!";
            }
            return new Message().setText(reply).setQuickReplies(buttons);
        } else {
            String reply = "Hãy chọn học kỳ mà bạn muốn xem điểm";
            for (PSListResponse aSemester : studentBasicTest.getPointListSemesterArr()) {
                String semesterDetail = "Điểm kỳ " + aSemester.getHocky() + ", " + aSemester.getNamhoc();
                listBtn.add(new Button().setContentType("text").setTitle(semesterDetail).setPayload(semesterDetail));
            }

            return new Message().setText(reply).setQuickReplies(this.ArrToBtnArr(listBtn));
        }
    }

    public Message viewSpecificPoint(String msg) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, IOException {
        if (this.getStudentIdByFid() == -1) {
            return this.login(this.definedStr.cboxInvalidCredentialsPlsLogAgain_PRODUCTION());
        }
        double diemHocjKyHe4 = 0, tinChiHocKy = 0;
        ArrayList<pointResponse> pointFullList;

        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);

        /*if (studentBasicTest.isServerReloading()) {
            return new Message().setText(this.definedStr.serverReloadingPlsTryAgainLab_PRODUCTION());
        }*/

        String reply = "Sau đây là danh sách điểm học kỳ bạn đã chọn\n-----------------------------";
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại"),
                new Button().setContentType("text").setTitle("Xem điểm").setPayload("Xem điểm"),
                new Button().setContentType("text").setTitle("Xem thời khóa biểu").setPayload("Xem thời khóa biểu"),
                new Button().setContentType("text").setTitle("Báo lỗi").setPayload("Báo lỗi")
        };
        synchronized (this._lock) {
            pointFullList = studentBasicTest.getPointBySemesterArr(msg);
        }
        if (pointFullList.size() != 0)
        {
            for (pointResponse point : pointFullList) {
                reply += "\n\nMôn " + point.getTenmon() + " (" + point.getTinchi() + " tín chỉ): điểm kiểm tra lần 1 *" + point.getDiemkt1() + "*, điểm kiểm tra lần 2 *" + point.getDiemkt2() + "*, điểm thi *" + point.getThil1() + "*, điểm tổng (thang điểm 4) *" + point.getTk4() + "* (Đạt loại *" + point.getTkch() + "*)";
                if (this.isNumeric(point.getTk4()) && this.isNumeric(point.getTinchi()))
                {
                    diemHocjKyHe4 += Double.parseDouble(point.getTk4());
                    tinChiHocKy += Double.parseDouble(point.getTinchi());
                }
            }
            diemHocjKyHe4 /= pointFullList.size();
            reply += "\n\nTổng kết:\n- Điểm trung bình học kỳ (thang 4): *" + diemHocjKyHe4 + "* (Xếp loại: " + this.rankByPoint(diemHocjKyHe4) + ")\n- Số tín chỉ đạt được trong học kỳ: " + tinChiHocKy ;
        }
        else reply = "Bạn chưa có điểm nào cả!";
        return new Message().setText(reply).setQuickReplies(buttons);
    }

    private String rankByPoint(double p4) {
        if (p4 < 2.) return "Yếu";
        else if (p4 >= 2. && p4 <= 2.49) return "Trung bình";
        else if (p4 > 2.49 && p4 <= 3.19) return "Khá";
        else if (p4 > 3.19 && p4 <= 3.59) return "Giỏi";
        else return "Xuất sắc";
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

    public boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
}
