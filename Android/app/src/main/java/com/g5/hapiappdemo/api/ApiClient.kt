package com.g5.hapiappdemo.api

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.g5.hapiappdemo.PreferenceConstants
import com.g5.hapiappdemo.extensions.PreferenceHelper
import com.g5.hapiappdemo.json.*
import io.reactivex.Observable
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/*
 * API Documentation? No
 * But you should take a look at: https://github.com/lesongvi/HapiApp/blob/main/README.md
 */

class ApiClient private constructor(context: Context) {
    private var restService: RestService? = null
    private var prefs: SharedPreferences? = null

    init {
        prefs = PreferenceHelper.securePrefs(context)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://f7e960290b34.ngrok.io")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        restService = retrofit.create(RestService::class.java)
    }

    companion object : SingletonHolder<ApiClient, Context>(::ApiClient)

    fun getToken(): String {
        return prefs?.getString(PreferenceConstants.token, "") ?: ""
    }

    fun getStudentID(): String {
        return prefs?.getString(PreferenceConstants.sid, "") ?: ""
    }

    fun getStudentEmail(): String {
        return prefs?.getString(PreferenceConstants.semail, "") ?: ""
    }

    fun getStudentName(): String {
        return prefs?.getString(PreferenceConstants.sname, "") ?: ""
    }

    fun getStudentP1(): String {
        return prefs?.getString(PreferenceConstants.sdt1, "") ?: ""
    }

    fun getStudentP2(): String {
        return prefs?.getString(PreferenceConstants.sdt2, "") ?: ""
    }

    fun isFingerAuth(): Boolean {
        return prefs?.getBoolean(PreferenceConstants.fingerLoginAccount, false) ?: true
    }

    fun getTheme(): String {
        return prefs?.getString(PreferenceConstants.theme, "") ?: ""
    }

    fun isReady(): Boolean {
        return this.restService != null
    }

    fun postLogin(sid: String, spd: String): Observable<AuthJson> {
        return restService!!.postLogin(sid, spd)
    }

    fun requestInfo(token: String): Observable<AuthJson> {
        return restService!!.requestInfo(token)
    }

    // region point view
    fun viewCurrentPoint(): Observable<List<PointJson>> {
        return restService!!.viewCurrentPoint(getToken())
    }

    fun viewSemesterPoint(): Observable<List<SemesterPoint>> {
        return restService!!.viewSemesterPoint(getToken())
    }

    fun viewSpecificPoint(semesterId: String?, semesterYear: String?): Observable<List<PointJson>> {
        return restService!!.viewSpecificPoint(getToken(), "Điểm kỳ $semesterId, $semesterYear")
    }
    // end region

    // region schedule view
    fun getScheduleSemester(): Observable<List<SemesterSche>> {
        return restService!!.getScheduleSemester(getToken())
    }

    fun viewSemesterScheW(semesterId: String): Observable<List<SemesterScheWeek>> {
        return restService!!.viewSemesterScheW(getToken(), semesterId)
    }

    fun viewSpecificSche(semesterId: String, weekId: String): Observable<List<SemesterScheDetail>> {
        return restService!!.viewSemesterScheD(getToken(), semesterId.trim(), weekId.trim())
    }
    // end region

    // region examsche view
    fun viewExamSche(): Observable<List<ExamScheDetail>> {
        return restService!!.viewExamSche(getToken())
    }
    // end region

    // region evaluate view
    fun viewEvaluateList(): Observable<List<EvaluateList>> {
        return restService!!.viewEvaluateList(getToken())
    }

    fun viewEvaluateList(ticketId: String): Observable<List<Any>> {
        return restService!!.viewEvaluateList(getToken(), ticketId)
    }
    // end region

    // region tarest
    fun viewTarestList(): Observable<List<Any>> {
        return restService!!.viewTarestList(getToken())
    }

    fun postTarest(mon: String, gvien: String, nnghi: String, lydo: String, ca: String): Observable<List<Any>> {
        return restService!!.postTarest(getToken(), mon, gvien, nnghi, lydo, ca)
    }
    // end region

    // region notification view
    fun viewLNotification(): Observable<NotificationList> {
        return restService!!.viewLNotification()
    }

    fun viewNotifications(): Observable<List<NotificationList>> {
        return restService!!.viewNotifications()
    }
    // end region
}