package com.g5.hapiappdemo.realmobj

import io.realm.RealmObject

open class semesterSDObj : RealmObject() {
    var lop: String? = ""
    var mon: String? = ""
    var ngaybd: String? = ""
    var ngaykt: String? = ""
    var nhom: String? = ""
    var phong: String? = ""
    var sotiet: String? = ""
    var thu: String? = ""
    var tietbatdau: String? = ""
    var tinchi: String? = ""
    var giangvien: String? = ""
    var un_c1: String? = ""
    var un_c2: String? = ""
}