package com.g5.hapiappdemo.realmobj

import com.g5.hapiappdemo.json.PointJson
import com.g5.hapiappdemo.json.SemesterPoint
import io.realm.RealmObject

open class studentPoint : RealmObject() {
    var semester: String? = null
    var data: String? = null
}
