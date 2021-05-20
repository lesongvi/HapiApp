package com.g5.hapiappdemo.realmobj

import io.realm.RealmObject

open class notificationObj : RealmObject() {
    var notification: String? = ""
    var unixtime: Long? = 0
}