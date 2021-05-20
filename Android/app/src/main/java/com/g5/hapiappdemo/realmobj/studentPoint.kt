package com.g5.hapiappdemo.realmobj

import io.realm.RealmObject

open class studentPoint : RealmObject() {
    var semester: String? = null
    var data: String? = null
}
