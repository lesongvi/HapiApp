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
import java.io.UnsupportedEncodingException;
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
        String reply = "B???n n??i v???y m??nh kh??ng hi???u :3 n???u b???n mu???n c?? t??nh n??ng m???i h??y li??n h??? qu???n l?? c???a m??nh qua email mail.cua.ql.hapi.ne@roleplay[.]vn (nh??? b??? d???u ch???m '.' gi???a roleplay v?? vn)";
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i"),
                new Button().setContentType("text").setTitle("B??o l???i").setPayload("B??o l???i")
        };

        return new Message().setText(reply).setQuickReplies(buttons);
    }

    public Message NotificationLookup(NotificationRespository notificationRespository, NotificationService notificationService) throws IOException {
        snapshotHelper snapshot = new snapshotHelper(notificationRespository, notificationService);
        String reply = snapshot.snapshotNotificationNotify().getNotification();
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i"),
                new Button().setContentType("text").setTitle("B??o l???i").setPayload("B??o l???i")
        };

        return new Message().setText(reply).setQuickReplies(buttons);
    }

    public Message currentWeekView() throws BadPaddingException, ParseException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        if (this.getStudentIdByFid() == -1) {
            return this.login("B???n ch??a ????ng nh???p ho???c m???t kh???u c???a b???n ???? b??? thay ?????i! \nNh???p v??o n??t *\"L??u t??i kho???n\"* ????? l??u t??i kho???n b???n nh??!\nSau khi ???? l??u xong b???n h??y g?? *B???t ?????u* ho???c nh???p n??t *B???t ?????u l???i*");
        }

        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i"),
                new Button().setContentType("text").setTitle("B??o l???i").setPayload("B??o l???i")
        };
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());


        if (studentCre.isPresent()) {
            browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);

            weekResponse selectedWeek;
            synchronized (_lock) {
                selectedWeek = studentBasicTest.findSelectedWeek();
            }

            return this.getScheduleByWeekAndSemester(selectedWeek.getTenxacdinh(), true);
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
            return this.login("B???n ch??a ????ng nh???p ho???c m???t kh???u c???a b???n ???? b??? thay ?????i! \nNh???p v??o n??t *\"L??u t??i kho???n\"* ????? l??u t??i kho???n b???n nh??!\nSau khi ???? l??u xong b???n h??y g?? *B???t ?????u* ho???c nh???p n??t *B???t ?????u l???i*");
        }
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i"),
                new Button().setContentType("text").setTitle("B??o l???i").setPayload("B??o l???i")
        };
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        String reply = "H??y ch???n h???c k??? b???n mu???n xem th???i kh??a bi???u";

        if (studentCre.isPresent()) {
            browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);
            ArrayList<Button> listBtn = new ArrayList<Button>();

            /*if (studentBasicTest.isServerReloading()) {
                return new Message().setText(this.definedStr.serverReloadingPlsTryAgainLab_PRODUCTION());
            }*/

            listBtn.add(new Button().setContentType("text").setTitle("TKB tu???n n??y").setPayload("TKB tu???n n??y"));

            for(semesterResponse semester : studentBasicTest.getSemesterArr()) {
                String currentId = "H???c k??? " + semester.semesterId.charAt(semester.semesterId.length() - 1) + ", " +  semester.semesterId.trim().substring(0, 4);
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

    public Message startWeekInitial(String semesterId) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, MalformedURLException, UnsupportedEncodingException {
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        String reply = "H??y g?? *s??? tu???n* b???n mu???n xem th???i kh??a bi???u v?? nh???p Enter\n-------------------";
        ArrayList<weekResponse> weekArr;
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i"),
                new Button().setContentType("text").setTitle("TKB tu???n n??y").setPayload("TKB tu???n n??y"),
                new Button().setContentType("text").setTitle("Xem th???i kh??a bi???u").setPayload("Xem th???i kh??a bi???u"),
                new Button().setContentType("text").setTitle("B??o l???i").setPayload("B??o l???i")
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
                    reply += "\nTu???n *" + Integer.parseInt(week.getSotuan()) + "* (t??? ng??y *" + week.getNgaybd() + "* ?????n ng??y *" + week.getNgaykt() + "*)";
                }
                reply += //"\nFacebook kh??ng cho ph??p c?? qu?? nhi???u n??t b???m trong 1 l???n, c??c b???n th??ng c???m gi??p Hapi nha <3!" +
                        "\n\nV?? d??? b???n mu???n xem th???i kh??a bi???u tu???n " + Integer.parseInt(weekArr.get(0).getSotuan()) + " (t??? ng??y " + weekArr.get(0).getNgaybd() + " - " + weekArr.get(0).getNgaykt() + ", h??y nh???p: " + Integer.parseInt(weekArr.get(0).getSotuan());
            }
            else reply = "B???n kh??ng c?? th???i kh??a bi???u trong h???c k??? n??y!";


            return new Message().setText(reply).setQuickReplies(buttons);
        }
        return new Message().setText(this.definedStr.pleaseTryAgainlab_PRODUCTION()).setQuickReplies(buttons);
    }

    private String getWeekByWeekNum(String weekNum) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, MalformedURLException, UnsupportedEncodingException {
        ChatbotStudents cbst = this.chatbotStudentService.findById(this.fid).get();
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);

        for(weekResponse week : studentBasicTest.getWeekArr(cbst.getCurrentSemesterId())) {
            if (Integer.parseInt(week.getSotuan()) == Integer.parseInt(weekNum))
                return week.getTenxacdinh();
        }

        return "";
    }

    private String getCurrentSemester() throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, MalformedURLException {
        ChatbotStudents cbst = this.chatbotStudentService.findById(this.fid).get();
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);

        return studentBasicTest.getSemesterArr().get(0).semesterId;
    }

    public Message howToUse() {
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u").setPayload("B???t ?????u"),
                new Button().setContentType("text").setTitle("B??o l???i").setPayload("B??o l???i")
        };
        String reply = "Hapi ch??o b???n nhaaa, b???n c?? th??? nh???n n??t *B???t ?????u* ????? b???t ?????u s??? d???ng ???ng d???ng.\n" +
                "Hapi l?? m???t bot gi??p sinh vi??n tr?????ng HUTECH c?? th??? d??? d??ng trong vi???c xem th??ng tin nh?? th???i kho?? bi???u, ??i???m thi c??c h???c k???, th??ng b??o m???i t??? nh?? tr?????ng...";

        return new Message().setText(reply).setQuickReplies(buttons);
    }

    public Message getScheduleByWeekAndSemester(String weekId, boolean isFormated) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, ParseException {
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i"),
                new Button().setContentType("text").setTitle("Xem th???i kh??a bi???u").setPayload("Xem th???i kh??a bi???u"),
                new Button().setContentType("text").setTitle("TKB tu???n n??y").setPayload("TKB tu???n n??y"),
                new Button().setContentType("text").setTitle("B??o l???i").setPayload("B??o l???i")
        };
        ArrayList<scheduleReponse> stdntList;
        String reply = "Sau ????y l?? th??ng tin th???i kh??a bi???u tu???n " + weekId + " c???a b???n:\n--------------------------------";

        if (studentCre.isPresent()) {
            ChatbotStudents cbst = this.chatbotStudentService.findById(this.fid).get();
            String semesterId = cbst.getCurrentSemesterId();

            if (!isFormated)
            {
                weekId = this.getWeekByWeekNum(weekId);
            } else {
                synchronized (_lock) {
                    semesterId = this.getCurrentSemester().trim();
                }
            }

            if (weekId == "")
                return new Message().setText(this.definedStr.pleaseTryAgainlab_PRODUCTION()).setQuickReplies(buttons);
            if (cbst.getCurrentSemesterId() == null)
                return new Message().setText(this.definedStr.youNeedToInitialize_PRODUCTION()).setQuickReplies(buttons);


            cbst.setCurrentWeekId(weekId);
            this.chatbotStudentService.save(cbst);

            synchronized(_lock) {
                browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);
                stdntList = studentBasicTest.getScheduleDetailArr(semesterId, weekId);
            }

            if (stdntList != null && stdntList.size() != 0)
                for (scheduleReponse scdule : stdntList) {
                    reply += "\n\n*" + scdule.thu + "* ti???t *" + scdule.tietbatdau + "* (" + this.convertTime(Integer.parseInt(scdule.tietbatdau)) + "): h???c *" + scdule.sotiet + " ti???t* m??n *" + scdule.mon + "* (" + scdule.tinchi + " t??n ch???) v???i, l???p *" + scdule.lop + "* (" + scdule.nhom + ") t???i ph??ng *" + scdule.phong + "*" + (scdule.giangvien.length() > 5 ? " do gi???ng vi??n " + scdule.giangvien + " d???y" : "") + ". B???n s??? h???c m??n n??y t??? ng??y " + scdule.ngaybd + " ?????n ng??y " + scdule.ngaykt + ".";
                }
            else reply = "B???n kh??ng c?? th???i kh??a bi???u trong tu???n n??y!";

            return new Message().setText(reply).setQuickReplies(buttons);
        }
        return new Message().setText(this.definedStr.pleaseTryAgainlab_PRODUCTION()).setQuickReplies(buttons);
    }

    private String encryptFid (String str) {
        String encoded = new String(Base64.getEncoder().encode(str.getBytes()));
        return encoded;
    }

    public Message login (String changeMyMind) {
        String reply = changeMyMind == "" ? "Nh???p v??o n??t *\"L??u t??i kho???n\"* ????? l??u t??i kho???n b???n nh??!\nSau khi ???? l??u xong b???n h??y g?? *B???t ?????u* ho???c nh???p n??t *B???t ?????u l???i*" : changeMyMind;
        Button[] buttons = new Button[]{
                new Button().setType("web_url").setUrl("https://h.2cll.com?fid=" + this.encryptFid(this.fid)).setTitle("L??u t??i kho???n")
        };

        Button[] buttons2 = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i")
        };

        return new Message().setAttachment(new Attachment().setType("template").setPayload(new Payload()
                .setTemplateType("button").setText(reply).setButtons(buttons))).setQuickReplies(buttons2);
    }

    public Message userConfigRequest () {
        if (this.getStudentIdByFid() == -1) {
            return this.login(this.definedStr.cboxInvalidCredentialsPlsLogAgain_PRODUCTION());
        }

        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i"),
                new Button().setContentType("text").setTitle("????ng xu???t").setPayload("????ng xu???t")
        };
        String reply = "H??y ch???n h??nh ?????ng b???n mu???n, b???t ?????u l???i ho???c ????ng xu???t t??y ?? huynh ^^";

        return new Message().setText(reply).setQuickReplies(buttons);
    }

    public Message showPointOpt() {
        if (this.getStudentIdByFid() == -1) {
            return this.login(this.definedStr.cboxInvalidCredentialsPlsLogAgain_PRODUCTION());
        }

        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("??i???m hi???n t???i").setPayload("??i???m hi???n t???i"),
                new Button().setContentType("text").setTitle("T???t c??? ??i???m").setPayload("T???t c??? ??i???m")
        };
        String reply = "H??y ch???n kh??ng gian b???n mu???n, *??i???m hi???n t???i* t???c l?? ??i???m c???a h???c k??? hi???n t???i, *t???t c??? ??i???m* t???c l?? ??i???m c???a t???t c??? h???c k??? ^^";

        return new Message().setText(reply).setQuickReplies(buttons);
    }

    public Message currentSessionDetail() {
        if (this.getStudentIdByFid() == -1) {
            return this.login(this.definedStr.cboxInvalidCredentialsPlsLogAgain_PRODUCTION());
        }

        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i"),
                new Button().setContentType("text").setTitle("C??i ?????t").setPayload("C??i ?????t")
        };

        String msgBack = "";
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        if(!studentCre.isPresent())
            msgBack += this.definedStr.pleaseTryAgainlab_PRODUCTION();
        else
            msgBack += "Th??ng tin t??i kho???n c???a b???n\nH??? t??n sinh vi??n: " + studentCre.get().getName() + "\nM?? s??? sinh vi??n: " + studentCre.get().getSid();

        return new Message().setText(msgBack).setQuickReplies(buttons);
    }

    public Message detailPointOpt(String msg) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, IOException {
        if (this.getStudentIdByFid() == -1) {
            return this.login(this.definedStr.cboxInvalidCredentialsPlsLogAgain_PRODUCTION());
        }
        double diemHocjKyHe4 = 0, tinChiHocKy = 0;
        ArrayList<pointResponse> pointFullList;
        ArrayList<PSListResponse> plSemester;
        String reply;

        ArrayList<Button> listBtn = new ArrayList<>();
        Optional<Students> studentCre = this.studentService.findById(this.getStudentIdByFid());
        browserHelper studentBasicTest = new browserHelper(studentCre.get().getToken(), this.studentRepository, this.studentService);
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i"),
                new Button().setContentType("text").setTitle("Xem ??i???m").setPayload("Xem ??i???m"),
                new Button().setContentType("text").setTitle("B??o l???i").setPayload("B??o l???i")
        };

        /*if (studentBasicTest.isServerReloading()) {
            return new Message().setText(this.definedStr.serverReloadingPlsTryAgainLab_PRODUCTION());
        }*/

        if (msg.toLowerCase().trim().equals("??i???m hi???n t???i"))
        {
            reply = "Sau ????y l?? danh s??ch ??i???m c???a b???n\n-----------------------------";
            synchronized (this._lock) {
                pointFullList = studentBasicTest.getCurrentPointArr();

                if (pointFullList.size() != 0)
                {
                    for (pointResponse point : pointFullList) {
                        reply += "\n\nM??n " + point.getTenmon() + " (" + point.getTinchi() + " t??n ch???): ??i???m ki???m tra l???n 1 *" + point.getDiemkt1() + "*, ??i???m ki???m tra l???n 2 *" + point.getDiemkt2() + "*, ??i???m thi *" + point.getThil1() + "*, ??i???m t???ng (thang ??i???m 4): *" + point.getTk4() + "* (?????t lo???i *" + point.getTkch() + "*)";
                        if (this.isNumeric(point.getTk4()) && this.isNumeric(point.getTinchi()))
                        {
                            diemHocjKyHe4 += Double.parseDouble(point.getTk4());
                            tinChiHocKy += Double.parseDouble(point.getTinchi());
                        }
                    }
                    diemHocjKyHe4 /= pointFullList.size();
                    reply += "\n\nT???ng k???t:\n- ??i???m trung b??nh h???c k??? (thang 4): *" + diemHocjKyHe4 + "* (X???p lo???i: " + this.rankByPoint(diemHocjKyHe4) + ")\n- S??? t??n ch??? ?????t ???????c trong h???c k???: " + tinChiHocKy ;
                }
                else reply = "B???n ch??a c?? ??i???m n??o c???!";
            }
            return new Message().setText(reply).setQuickReplies(buttons);
        } else {
            Button[] rederedBtns = buttons;
            reply = "???? c?? l???i x???y ra, b???n vui l??ng xem ??i???m sau 1 l??t n???a nh?? @@~";
            synchronized (this._lock) {
                plSemester = studentBasicTest.getPointListSemesterArr();
                for (PSListResponse aSemester : plSemester) {
                    String semesterDetail = "??i???m k??? " + aSemester.getHocky() + ", " + aSemester.getNamhoc();
                    listBtn.add(new Button().setContentType("text").setTitle(semesterDetail).setPayload(semesterDetail));
                }

                if(listBtn.size() != 0) {
                    reply = "H??y ch???n h???c k??? m?? b???n mu???n xem ??i???m";
                    rederedBtns = this.ArrToBtnArr(listBtn);
                }
            }

            return new Message().setText(reply).setQuickReplies(rederedBtns);
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

        String reply = "Sau ????y l?? danh s??ch ??i???m h???c k??? b???n ???? ch???n\n-----------------------------";
        Button[] buttons = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i"),
                new Button().setContentType("text").setTitle("Xem ??i???m").setPayload("Xem ??i???m"),
                new Button().setContentType("text").setTitle("Xem th???i kh??a bi???u").setPayload("Xem th???i kh??a bi???u"),
                new Button().setContentType("text").setTitle("B??o l???i").setPayload("B??o l???i")
        };
        synchronized (this._lock) {
            pointFullList = studentBasicTest.getPointBySemesterArr(msg);
        }
        if (pointFullList.size() != 0)
        {
            for (pointResponse point : pointFullList) {
                reply += "\n\nM??n " + point.getTenmon() + " (" + point.getTinchi() + " t??n ch???): ??i???m ki???m tra l???n 1 *" + point.getDiemkt1() + "*, ??i???m ki???m tra l???n 2 *" + point.getDiemkt2() + "*, ??i???m thi *" + point.getThil1() + "*, ??i???m t???ng (thang ??i???m 4) *" + point.getTk4() + "* (?????t lo???i *" + point.getTkch() + "*)";
                if (this.isNumeric(point.getTk4()) && this.isNumeric(point.getTinchi()))
                {
                    diemHocjKyHe4 += Double.parseDouble(point.getTk4());
                    tinChiHocKy += Double.parseDouble(point.getTinchi());
                }
            }
            diemHocjKyHe4 /= pointFullList.size();
            reply += "\n\nT???ng k???t:\n- ??i???m trung b??nh h???c k??? (thang 4): *" + diemHocjKyHe4 + "* (X???p lo???i: " + this.rankByPoint(diemHocjKyHe4) + ")\n- S??? t??n ch??? ?????t ???????c trong h???c k???: " + tinChiHocKy ;
        }
        else reply = "Hi???n t???i h???c k??? n??y v???n ch??a c?? ??i???m :(! H??y xem l???i sau b???n nh??!!";
        return new Message().setText(reply).setQuickReplies(buttons);
    }

    private String rankByPoint(double p4) {
        if (p4 < 2.) return "Y???u";
        else if (p4 >= 2. && p4 <= 2.49) return "Trung b??nh";
        else if (p4 > 2.49 && p4 <= 3.19) return "Kh??";
        else if (p4 > 3.19 && p4 <= 3.59) return "Gi???i";
        else return "Xu???t s???c";
    }

    public Message userConfigCome () {
        if (this.getStudentIdByFid() == -1) {
            return this.login(this.definedStr.cboxInvalidCredentialsPlsLogAgain_PRODUCTION());
        }

        this.chatbotStudentService.findById(this.fid).ifPresent(cbstdnt -> {
            cbstdnt.setSid(0);
            this.chatbotStudentService.save(cbstdnt);
        });

        String reply = "????ng xu???t t??i kho???n th??nh c??ng kh???i h??? th???ng Hapi :( _T???i sao l???i b??? m??nh th??? huhu_. Nh??ng m?? kh??ng sao h???t ??, b???n c?? th??? ????ng nh???p l???i b???t c??? l??c n??o.\nXin h??y gi???i thi???u cho b???n b?? h???c chung t???i HUTECH ????? h??? c?? th??? bi???t v??? d???ch v??? mi???n ph?? v?? h???u ??ch n??y nh?? <3 <3";

        return new Message().setText(reply);
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
