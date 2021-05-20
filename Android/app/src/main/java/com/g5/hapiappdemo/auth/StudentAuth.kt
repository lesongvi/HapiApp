package com.g5.hapiappdemo.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.g5.hapiappdemo.MainActivity
import com.g5.hapiappdemo.PreferenceConstants
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.activities.BaseActivity
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.models.StudentAuthModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_signin.*
import org.json.JSONException
import com.g5.hapiappdemo.databinding.ActivitySigninBinding
import com.g5.hapiappdemo.extensions.PreferenceHelper.edit
import com.g5.hapiappdemo.extensions.PreferenceHelper.securePrefs
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.*
import java.util.*


class StudentAuth : BaseActivity() {
    private var studentIdIpt: TextInputEditText? = null
    private var studentPwdIpt: TextInputEditText? = null
    private var tm: TelephonyManager? = null
    private var button_to_site: TextView? = null
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var saveButton: Button? = null
    var PRIVATE_MODE = 0

    private var disposable: Disposable? = null
    private lateinit var binding: ActivitySigninBinding

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref?.edit()
        tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?

        studentIdIpt = binding.studentIdIpt
        studentPwdIpt = binding.studentPwdIpt
        saveButton = button_save
        button_to_site = button_to_site

        val window: Window = this@StudentAuth.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@StudentAuth, R.color.MMPrimary)

        saveButton!!.setOnClickListener { _ -> saveData() }
    }

    private val deviceName: String
        get() {
            val manufacturer: String = Build.MANUFACTURER
            val model: String = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else {
                capitalize(manufacturer) + " " + model
            }
        }

    private fun capitalize(s: String?): String {
        if (s == null || s.length == 0) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }

    private fun LoginNow(sid: String?, spd: String?) {
        val prefs = securePrefs(this)
        if (ApiClient.getInstance(this).isReady()) {
            disposable = ApiClient.getInstance(this).postLogin(sid!!, spd!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        if (result.error != true) {
                            prefs[PreferenceConstants.sid] = result.sid
                            prefs[PreferenceConstants.sname] = result.tensv
                            prefs[PreferenceConstants.token] = result.token
                            prefs[PreferenceConstants.semail] = result.email
                            prefs[PreferenceConstants.sdt1] = result.sdt1
                            prefs[PreferenceConstants.sdt2] = result.sdt2
                            prefs[PreferenceConstants.loggedIn] = true

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            var msg: String = resources.getString(R.string.auth_invalid_credentials)
                            if (result.message != null) msg = result.message
                            binding.studentIdIpt.isEnabled = true
                            Toast.makeText(
                                this@StudentAuth,
                                msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        hideProgressDialog()
                    },
                    { _ ->
                        hideProgressDialog()
                        binding.studentIdIpt.isEnabled = true
                        Toast.makeText(
                            this@StudentAuth,
                            resources.getString(R.string.error_fallback_msg),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
        }
    }

    private fun authProcess(jsonObj: JSONObject) {
        val creData = StudentAuthModel()
        try {
            if (jsonObj.has("token") && !jsonObj.getString("error").toBoolean()) {
                try {
                    creData.studentName = jsonObj.getString("tensv")
                    creData.studentToken = jsonObj.getString("token")
                    creData.lastLoginDate = System.currentTimeMillis()

                    val out: FileOutputStream =
                        this@StudentAuth.openFileOutput(R.string.auth_file_name.toString(), MODE_PRIVATE)
                    out.write(creData.studentToken!!.toByteArray())
                    out.close()
                    startActivity(Intent(this@StudentAuth, MainActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@StudentAuth,
                        resources.getString(R.string.error_fallback_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (jsonObj.getString("error").toBoolean()) {
                var msg: String = resources.getString(R.string.error_fallback_msg)
                if (jsonObj.has("message")) msg = "Lỗi: ${jsonObj.getString("message")}"
                Toast.makeText(
                    this@StudentAuth,
                    msg,
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: JSONException) {
            Toast.makeText(
                this@StudentAuth, e.toString(),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun saveData() {
        if (!validate()) return

        showProgressDialog()

        val studentId: String = binding.studentIdIpt.text.toString()
        val studentPwd: String = binding.studentPwdIpt.text.toString()
        try {
            if (studentId.length == 10) {
                LoginNow(studentId, studentPwd)
            } else {
                hideProgressDialog()
                Toast.makeText(
                    this,
                    resources.getString(R.string.error_invalid_credentials_msg),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            hideProgressDialog()
            Toast.makeText(
                this,
                resources.getString(R.string.error_fallback_msg),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun validate(): Boolean {
        var valid = true

        if (binding.studentIdIpt.text!!.isEmpty()) {
            binding.studentIdIpt.error = resources.getString(R.string.sid_required)
            valid = false
        } else {
            binding.studentIdIpt.error = null
        }

        if (binding.studentPwdIpt.text!!.isEmpty()) {
            binding.studentPwdIpt.error = resources.getString(R.string.spwd_required)
            valid = false
        } else {
            binding.studentPwdIpt.error = null
        }

        return valid
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    companion object {
        private const val RC_SIGN_IN = 1
        private const val PREF_NAME = "hapi_app"
    }
}

operator fun SharedPreferences.set(key: String, value: Any?) {
    when (value) {
        is String? -> edit({ it.putString(key, value) })
        is Int -> edit({ it.putInt(key, value) })
        is Boolean -> edit({ it.putBoolean(key, value) })
        is Float -> edit({ it.putFloat(key, value) })
        is Long -> edit({ it.putLong(key, value) })
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}
