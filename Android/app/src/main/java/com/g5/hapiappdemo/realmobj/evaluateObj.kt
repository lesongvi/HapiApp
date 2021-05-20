package com.g5.hapiappdemo.realmobj

import io.realm.RealmObject

open class evaluateObj : RealmObject() {
    var diem_ca_nhan: String? = ""
    var diem_khoa: String? = ""
    var diem_lop: String? = ""
    var dot_khao_sat_id: String? = ""
    var hienThiDiemTong: Boolean? = false
    var hoc_ky: String? = ""
    var nam_hoc: String? = ""
    var ngay_bd_sinhvien: String? = ""
    var ngay_kt_sinhvien: String? = ""
    var phieu_danh_gia_id: String? = ""
    var trang_thai_duyet: Int? = 0
    var xep_loai: String? = ""
}