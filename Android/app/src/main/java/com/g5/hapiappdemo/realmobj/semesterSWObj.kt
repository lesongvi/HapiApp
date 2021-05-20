package com.g5.hapiappdemo.realmobj

import io.realm.RealmObject

open class semesterSWObj : RealmObject() {
    var ngaybd: String? = ""
    var ngaykt: String? = ""
    var sotuan: String? = ""
    var tenxacdinh: String? = ""
    var unixtimebd: Long? = 0
    var unixtimekt: Long? = 0
}