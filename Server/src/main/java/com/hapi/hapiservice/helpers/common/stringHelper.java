package com.hapi.hapiservice.helpers.common;

import java.net.URLEncoder;

public class stringHelper {
    final private String defaultPage = "http://daotao.hutech.edu.vn/default.aspx";
    final private String defaultPageWithUppercaseD = "http://daotao.hutech.edu.vn/Default.aspx";
    final private String studentApi = "https://api.hutech.edu.vn/";
    final private String loginId = "ctl00_ContentPlaceHolder1_ctl00_ucDangNhap_txtTaiKhoa";
    final private String loginPwdId = "ctl00_ContentPlaceHolder1_ctl00_ucDangNhap_txtMatKhau";
    final private String loginPwdName = "ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$txtMatKhau";
    final private String loginBtnId = "ctl00_ContentPlaceHolder1_ctl00_ucDangNhap_btnDangNhap";
    final private String userAgentDefault =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
    final private String viewStatePattern = "\"__VIEWSTATE\" value=\"(.*)\"";
    final private String invalidCredentialsPattern = "Sai thông tin đăng nhập";
    final private String schedulePageUrlNoMode = this.defaultPage + "?page=thoikhoabieu&sta=";
    final private String schedulePageUrl = this.schedulePageUrlNoMode + "0";
    final private String schedulePageUrlMode1 = this.schedulePageUrlNoMode + "1";
    final private String semesterOptId = "ctl00_ContentPlaceHolder1_ctl00_ddlChonNHHK";
    final private String weekOptId = "ctl00_ContentPlaceHolder1_ctl00_ddlTuan";
    final private String selectOptPattern = "<([\\w]+)[^>]*>(.*?)<\\/\\1>";
    final private String miniOptPattern = "<option [^>]*value=\"(.*?)\">";
    final private String selectedMiniOptPattern = "<option selected=\"selected\" value=\"(.*?)\">";
    final private String weekNumOptPattern = "Tuần (.*) \\[";
    final private String startDateOptPattern = "Từ (.*) --";
    final private String endDateOptPattern = "Đến (.*)\\]";
    final private String schedulePattern = "\"ddrivetip\\((.*?) *,'Mã M";
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
    final private String studentPListPattern = "title-hk-diem[^>]*>\\s*<((?!<)(.|\\n))*?\\>\\s*<((?!<)(.|\\n))*?\\> *Học kỳ (.*?) - Năm học (.*?)<\\/";
    final private String studentPRangeSelectPattern = "\\s*<\\/((?!<)(.|\\n))*?\\>\\s*<((?!<)(.|\\n))*?\\>\\s*<((?!<)(.|\\n))*?\\>(.*?)<((?!<)(.|\\n))*class=\"row-diemTK\"";
    final private String cbotLogin = "Đăng nhập";
    final private String cboxStartFirstInitialPattern = "(Thời khóa biểu của tôi|Xem thời khóa biểu)";
    final private String pleaseTryAgainlab = "Đã có lỗi xảy ra, vui lòng thử lại trong 15 phút nữa!";
    final private String cboxSemesterCheckPattern = "Học kỳ .* - Năm .*";
    final private String cboxExtractSemesterPattern = "Học kỳ (.*), (.*)";
    final private String cboxExtractWeekPattern = "Tuần (.*?) \\(từ ngày (.*?) đến ngày (.*?)\\)";
    final private String cboxInvalidCredentialsPlsLogAgain = "Bạn chưa đăng nhập hoặc mật khẩu của bạn đã bị thay đổi! \nNhấp vào nút *\"Lưu tài khoản\"* để lưu tài khoản bạn nhé!\nSau khi đã lưu xong bạn hãy gõ *Bắt đầu* hoặc nhấp nút *Bắt đầu lại*";
    final private String cboxPointTestPattern = "Điểm kỳ (.*?), (.*?)-(.*)";
    final private String cboxFirstTime = "Bạn ơi, đây là lần đầu tiên bạn sử dụng Hapi, hãy đăng nhập lần đầu bạn nhé!?";
    final private String notificationPattern = "window\\.onload=function\\(\\)\\{alert\\('(.*)'\\);\\}";
    final private String serverReloadPattern = "window.onload=function(){alert('Server đang tải lại dữ liệu. Vui lòng trở lại sau 15 phút!');}";
    final private String serverReloadingPlsTryAgainLab = "Máy chủ đang bảo trì để cập nhật dữ liệu sinh viên, vui lòng thử lại sau 15 phút";
    final private String echoPattern = "nói (.*)";
    final private String requestBodySSO = "__EVENTTARGET=ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK&__EVENTARGUMENT=&__LASTFOCUS=&ctl00$ContentPlaceHolder1$ctl00$ddlLoai=0&__VIEWSTATE={{VIEWSTATE}}&ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK={{CHON_NHHK}}&ctl00$ContentPlaceHolder1$ctl00$rad_MonHoc=rad_MonHoc";
    final private String requestBodySWO = "__EVENTTARGET=ctl00$ContentPlaceHolder1$ctl00$ddlTuan&__EVENTARGUMENT=&__LASTFOCUS=&ctl00$ContentPlaceHolder1$ctl00$ddlLoai=0&__VIEWSTATE={{VIEWSTATE}}&ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK={{CHON_NHHK}}&ctl00$ContentPlaceHolder1$ctl00$ddlLoai=0&ctl00$ContentPlaceHolder1$ctl00$ddlTuan={{DDL_TUAN}}";
    final private String requestBodyVAP = "__EVENTTARGET=ctl00$ContentPlaceHolder1$ctl00$lnkChangeview2&__EVENTARGUMENT=&__LASTFOCUS=&ctl00$ContentPlaceHolder1$ctl00$txtChonHK=&__VIEWSTATE={{VIEWSTATE}}";
    final private String youNeedToInitialize = "Xin lỗi nhưng đây là lần sử dụng đầu tiên của bạn! Bạn phải nhấp vào *Xem thời khóa biểu* sau đó chọn _học kỳ_ bạn muốn.\nSau đó bạn có thể xem thời khóa biểu tự do theo học kỳ đã chọn ^^";
    final private String acceptLanguage = "en,vi;q=0.9,en-US;q=0.8,tr;q=0.7";
    final private String acceptEncoding = "gzip, deflate";
    final private String reqOrigin = "http://daotao.hutech.edu.vn";
    final private String acceptDataType = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9";
    final private String studentEvaluateSearchByStudent = this.studentApi + "ren-luyen-sinh-vien/api/phieudanhgia/searchsinhvien";
    final private String studentEvaluateGetTicket = this.studentApi + "ren-luyen-sinh-vien/api/phieudanhgia/searchphieuchitietsinhvien";
    final private String studentTakeaRest = this.studentApi + "corona/api/nghi-phep-sinh-vien";
    final private String studentAuthApi = this.studentApi + "authentication/api/auth/login";
    final private String studentRestList = this.studentApi + "corona/api/nghi-phep-sinh-vien/get-list";
    final private String locationHeader = "/default.aspx?page=thoikhoabieu&sta=0";
    final private String examSchePageUrl = this.defaultPageWithUppercaseD + "?page=xemlichthi";
    final private String examTableId = "ctl00_ContentPlaceHolder1_ctl00_gvXem";
    final private String examValuPattern = "<span [^>]*>(.*?)<\\/span>";
    final private String notevnCloudA1 = "https://notevn.com/file/hapi_internal.htm";
    final private String studentDetailUrl = this.studentApi + "ren-luyen-sinh-vien/api/sinh-vien/get-by-mssv";
    final private String routeNationUrl = this.studentApi + "cau-hinh-chung/api/cauhinhchung/get-all-tinh-huyen-dan-toc-ton-giao";
    final private String getMessInterrupt = "MessUpdate\\('(.*?)'\\)";
    final private String surveyListUrl = this.studentApi + "khao-sat-sinh-vien/api/khao-sat-sinh-vien/get-list-active";
    final private String surveyDetailUrl = this.studentApi + "khao-sat-sinh-vien/api/khao-sat-sinh-vien/chi-tiet-phieu-danh-gia";
    final private String takeSurveyUrl = this.studentApi + "khao-sat-sinh-vien/api/khao-sat-sinh-vien/update-phieu-danh-gia";

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

    public String schedulePageUrlMode1_PRODUCTION() {
        return this.schedulePageUrlMode1;
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

    public String selectedMiniOptPattern_PRODUCTION() {
        return this.selectedMiniOptPattern;
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

    public String cbotLogin_PRODUCTION() {
        return this.cbotLogin;
    }

    public String cboxStartFirstInitialPattern_PRODUCTION() {
        return this.cboxStartFirstInitialPattern;
    }

    public String pleaseTryAgainlab_PRODUCTION() {
        return this.pleaseTryAgainlab;
    }

    public String cboxSemesterCheckPattern_PRODUCTION() {
        return this.cboxSemesterCheckPattern;
    }

    public String cboxExtractSemesterPattern_PRODUCTION() {
        return this.cboxExtractSemesterPattern;
    }

    public String cboxExtractWeekPattern_PRODUCTION() {
        return this.cboxExtractWeekPattern;
    }

    public String cboxInvalidCredentialsPlsLogAgain_PRODUCTION() {
        return this.cboxInvalidCredentialsPlsLogAgain;
    }

    public String cboxPointTestPattern_PRODUCTION() {
        return this.cboxPointTestPattern;
    }

    public String cboxFirstTime_PRODUCTION() {
        return this.cboxFirstTime;
    }

    public String notificationPattern_PRODUCTION() {
        return this.notificationPattern;
    }

    public String serverReloadPattern_PRODUCTION() {
        return this.serverReloadPattern;
    }

    public String serverReloadingPlsTryAgainLab_PRODUCTION() {
        return this.serverReloadingPlsTryAgainLab;
    }

    public String echoPattern_PRODUCTION() {
        return this.echoPattern;
    }

    public String requestBodySSO_PRODUCTION() {
        return this.requestBodySSO;
    }

    public String requestBodySWO_PRODUCTION() {
        return this.requestBodySWO;
    }

    public String requestBodyVAP_PRODUCTION() {
        return this.requestBodyVAP;
    }

    public String youNeedToInitialize_PRODUCTION() {
        return this.youNeedToInitialize;
    }

    public String acceptLanguage_PRODUCTION() {
        return this.acceptLanguage;
    }

    public String acceptEncoding_PRODUCTION() {
        return this.acceptEncoding;
    }

    public String acceptDataType_PRODUCTION() {
        return this.acceptDataType;
    }

    public String studentEvaluateSearchByStudent_PRODUCTION() {
        return this.studentEvaluateSearchByStudent;
    }

    public String studentEvaluateGetTicket_PRODUCTION() {
        return this.studentEvaluateGetTicket;
    }

    public String studentTakeaRest_PRODUCTION() {
        return this.studentTakeaRest;
    }

    public String studentAuthApi_PRODUCTION() {
        return this.studentAuthApi;
    }

    public String studentRestList_PRODUCTION() {
        return this.studentRestList;
    }
    
    public String locationHeader_PRODUCTION() {
        return this.locationHeader;
    }

    public String reqOrigin_PRODUCTION() {
        return this.reqOrigin;
    }

    public String examSchePageUrl_PRODUCTION() {
        return this.examSchePageUrl;
    }

    public String examTableId_PRODUCTION() {
        return this.examTableId;
    }

    public String examValuPattern_PRODUCTION() {
        return this.examValuPattern;
    }

    public String notevnCloudA1_PRODUCTION() {
        return this.notevnCloudA1;
    }

    public String studentDetailUrl_PRODUCTION() {
        return this.studentDetailUrl;
    }

    public String routeNationUrl_PRODUCTION() {
        return this.routeNationUrl;
    }

    public String getMessInterrupt_PRODUCTION() {
        return this.getMessInterrupt;
    }

    public String surveyListUrl_PRODUCTION() {
        return this.surveyListUrl;
    }

    public String surveyDetailUrl_PRODUCTION() {
        return this.surveyDetailUrl;
    }

    public String takeSurveyUrl_PRODUCTION() {
        return this.takeSurveyUrl;
    }
}
