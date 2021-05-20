package com.g5.hapiappdemo.models

class StudentAuthModel {
    var studentName: String? = null
    var studentToken: String? = null
    var lastLoginDate: Long = 0

    fun StudentAuthModel(
        studentName: String?,
        studentToken: String?,
        lastLoginDate: Long
    ) {
        this.studentName = studentName
        this.studentToken = studentToken
        this.lastLoginDate = lastLoginDate
    }
}