package com.g5.hapiappdemo.realmobj

import io.realm.RealmObject

open class semesterSObj : RealmObject() {
    var semesterId: String? = ""
    var semesterDetail: String? = ""
}