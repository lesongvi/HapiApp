package com.g5.hapiappdemo.api

import com.g5.hapiappdemo.json.*
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RestService {
    @FormUrlEncoded
    @POST("api/v1/auth")
    fun postLogin(
        @Field("sid") studentId: String,
        @Field("pwd") studentPwd: String
    ): Observable<AuthJson>

    @FormUrlEncoded
    @POST("api/v1/student/info")
    fun requestInfo(
        @Field("token") token: String
    ): Observable<AuthJson>

    @FormUrlEncoded
    @POST("api/v1/point/current")
    fun viewCurrentPoint(
        @Field("token") token: String
    ): Observable<List<PointJson>>

    @FormUrlEncoded
    @POST("api/v1/point/semester")
    fun viewSemesterPoint(
        @Field("token") token: String
    ): Observable<List<SemesterPoint>>

    @FormUrlEncoded
    @POST("api/v1/point/view")
    fun viewSpecificPoint(
        @Field("token") token: String,
        @Field("semesterId") semesterId: String
    ): Observable<List<PointJson>>

    @FormUrlEncoded
    @POST("api/v1/schedule/semester")
    fun getScheduleSemester(
        @Field("token") token: String
    ): Observable<List<SemesterSche>>

    @FormUrlEncoded
    @POST("api/v1/schedule/week")
    fun viewSemesterScheW(
        @Field("token") token: String,
        @Field("semesterId") semesterId: String
    ): Observable<List<SemesterScheWeek>>

    @FormUrlEncoded
    @POST("api/v1/schedule/detail")
    fun viewSemesterScheD(
        @Field("token") token: String,
        @Field("semesterId") semesterId: String,
        @Field("weekId") weekId: String
    ): Observable<List<SemesterScheDetail>>

    @FormUrlEncoded
    @POST("api/v1/schedule/cexam")
    fun viewExamSche(
        @Field("token") token: String
    ): Observable<List<ExamScheDetail>>

    @FormUrlEncoded
    @POST("api/v1/student/evaluate/list")
    fun viewEvaluateList(
        @Field("token") token: String
    ): Observable<List<EvaluateList>>

    @FormUrlEncoded
    @POST("api/v1/student/evaluate/view")
    fun viewEvaluateList(
        @Field("token") token: String,
        @Field("ticket") ticketId: String
    ): Observable<List<Any>>

    @FormUrlEncoded
    @POST("api/v1/student/tarest/list")
    fun viewTarestList(
        @Field("token") token: String
    ): Observable<List<Any>>

    @FormUrlEncoded
    @POST("api/v1/student/tarest/request")
    fun postTarest(
        @Field("token") token: String,
        @Field("monhoc") mon: String,
        @Field("giangvien") gvien: String,
        @Field("ngaynghi") nnghi: String,
        @Field("lydo") lydo: String,
        @Field("ca") ca: String
    ): Observable<List<Any>>

    @GET("api/v1/snapshot/notification")
    fun viewLNotification(): Observable<NotificationList>

    @GET("api/v1/snapshot/notifications")
    fun viewNotifications(): Observable<List<NotificationList>>

    @FormUrlEncoded
    @POST("api/v1/app/avatar/modify")
    fun uploadAvatar(
        @Field("token") token: String,
        @Field("base64") base64: String
    ): Observable<AvatarModify>
}