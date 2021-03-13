package com.hapi.hapiservice.helpers;

public class stringHelper {
    final private String defaultPage = "http://daotao.hutech.edu.vn/default.aspx";
    final private String defaultPageWithUppercaseD = "http://daotao.hutech.edu.vn/Default.aspx";
    final private String loginId = "ctl00_ContentPlaceHolder1_ctl00_ucDangNhap_txtTaiKhoa";
    final private String loginPwdId = "ctl00_ContentPlaceHolder1_ctl00_ucDangNhap_txtMatKhau";
    final private String loginPwdName = "ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$txtMatKhau";
    final private String loginBtnId = "ctl00_ContentPlaceHolder1_ctl00_ucDangNhap_btnDangNhap";
    final private String userAgentDefault =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
    final private String viewStatePattern = "\"__VIEWSTATE\" value=\"(.*)\"";
    final private String invalidCredentialsPattern = "Sai thông tin đăng nhập";
    final private String schedulePageUrl = this.defaultPage + "?page=thoikhoabieu&sta=0";
    final private String semesterOptId = "ctl00_ContentPlaceHolder1_ctl00_ddlChonNHHK";
    final private String weekOptId = "ctl00_ContentPlaceHolder1_ctl00_ddlTuan";
    final private String selectOptPattern = "<([\\w]+)[^>]*>(.*?)<\\/\\1>";
    final private String miniOptPattern = "<option value=\"(.*?)\">";
    final private String weekNumOptPattern = "Tuần (.*) \\[";
    final private String startDateOptPattern = "Từ (.*) --";
    final private String endDateOptPattern = "Đến (.*)\\]";
    final private String schedulePattern = "\"ddrivetip\\((.*?)\\)\\\"";
    final private String scheduleDescRemove = ",'Mã Môn Học-Tên Môn Học-Phòng Học-Thứ-Tiết Bắt Đầu-Số Tiết-Giảng Viên-Bắt Đầu Từ: - Đến -Lớp'";
    final private String scheduleTableId = "ctl00_ContentPlaceHolder1_ctl00_Table1";
    final private String helloStudentId = "ctl00_Header1_Logout1_lblNguoiDung";
    final private String studentNamePattern = "Chào bạn (.*?) \\(";
    final private String userInfoChangerUrl = this.defaultPageWithUppercaseD + "?page=thaydoittcn";
    final private String studentPointUrl = this.defaultPageWithUppercaseD + "?page=xemdiemthi";
    final private String studentEmailId = "ctl00_ContentPlaceHolder1_ctl00_txtEmail1";
    final private String studentSdt1Id = "ctl00_ContentPlaceHolder1_ctl00_txtDT1";
    final private String studentSdt2Id = "ctl00_ContentPlaceHolder1_ctl00_txtDT2";
    final private String studentCPointTblId = "ctl00_ContentPlaceHolder1_ctl00_div1";
    final private String studentPointPattern = "<span[^>]*>(?! *STT| *Học kỳ| *Thi L1| *Điểm | *Mã Môn| *Tên Môn| *TC| *\\% | *TK\\(*)(.*?)<\\/s";
    final private String studentPSemesterId = "ctl00_ContentPlaceHolder1_ctl00_div1";
    final private String studentPListPattern = "title-hk-diem[^>]*>\\s*<((?!<)(.|\\n))*?\\>\\s*<((?!<)(.|\\n))*?\\>(.*?)<\\/";
    final private String studentPRangeSelectPattern = "\\s*<\\/((?!<)(.|\\n))*?\\>\\s*<((?!<)(.|\\n))*?\\>\\s*<((?!<)(.|\\n))*?\\>(.*?)<((?!<)(.|\\n))*class=\"row-diemTK\"";

    public String defaultPage_PRODUCTION() {
        return this.defaultPage;
    }

    public String loginId_PRODUCTION() {
        return this.loginId;
    }

    public String loginPwdId_PRODUCTION() {
        return this.loginPwdId;
    }

    public String loginPwdName_PRODUCTION() {
        return this.loginPwdName;
    }

    public String loginBtnId_PRODUCTION() {
        return this.loginBtnId;
    }

    public String userAgentDefault_PRODUCTION() {
        return this.userAgentDefault;
    }

    public String viewStatePattern_PRODUCTION() {
        return this.viewStatePattern;
    }

    public String invalidCredentialsPattern_PRODUCTION() {
        return this.invalidCredentialsPattern;
    }

    public String schedulePage_PRODUCTION() {
        return this.schedulePageUrl;
    }

    public String semesterOptId_PRODUCTION() {
        return this.semesterOptId;
    }

    public String weekOptId_PRODUCTION() {
        return this.weekOptId;
    }

    public String semesterOptPattern_PRODUCTION() {
        return this.selectOptPattern;
    }

    public String weekOptPattern_PRODUCTION() {
        return this.selectOptPattern;
    }

    public String miniOptPattern_PRODUCTION() {
        return this.miniOptPattern;
    }

    public String weekNumOptPattern_PRODUCTION() {
        return this.weekNumOptPattern;
    }

    public String startDateOptPattern_PRODUCTION() {
        return this.startDateOptPattern;
    }

    public String endDateOptPattern_PRODUCTION() {
        return this.endDateOptPattern;
    }

    public String schedulePattern_PRODUCTION() {
        return this.schedulePattern;
    }

    public String scheduleTableId_PRODUCTION() {
        return this.scheduleTableId;
    }

    public String scheduleDescRemove_PRODUCTION() {
        return this.scheduleDescRemove;
    }

    public String helloStudentId_PRODUCTION() {
        return this.helloStudentId;
    }

    public String studentNamePattern_PRODUCTION() {
        return this.studentNamePattern;
    }

    public String userInfoChangerUrl_PRODUCTION() {
        return this.userInfoChangerUrl;
    }

    public String studentEmailId_PRODUCTION() {
        return this.studentEmailId;
    }

    public String studentSdt1Id_PRODUCTION() {
        return this.studentSdt1Id;
    }

    public String studentSdt2Id_PRODUCTION() {
        return this.studentSdt2Id;
    }

    public String studentPointUrl_PRODUCTION() {
        return this.studentPointUrl;
    }

    public String studentCPointTblId_PRODUCTION() {
        return this.studentCPointTblId;
    }

    public String studentPointPattern_PRODUCTION() {
        return this.studentPointPattern;
    }

    public String studentPSemesterId_PRODUCTION() {
        return this.studentPSemesterId;
    }

    public String studentPListPattern_PRODUCTION() {
        return this.studentPListPattern;
    }

    public String studentPRangeSelectPattern_PRODUCTION() {
        return this.studentPRangeSelectPattern;
    }
}
