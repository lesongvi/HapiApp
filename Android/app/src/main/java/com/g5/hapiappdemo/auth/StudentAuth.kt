package com.g5.hapiappdemo.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.biometrics.BiometricManager;
import androidx.biometric.BiometricPrompt;
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.telephony.TelephonyManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.g5.hapiappdemo.MainActivity
import com.g5.hapiappdemo.PreferenceConstants
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.activities.BaseActivity
import com.g5.hapiappdemo.activities.ForgotPasswordActivity
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.databinding.ActivitySigninBinding
import com.g5.hapiappdemo.extensions.PreferenceHelper.edit
import com.g5.hapiappdemo.extensions.PreferenceHelper.securePrefs
import com.g5.hapiappdemo.models.StudentAuthModel
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_signin.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*
import java.util.concurrent.Executor


class StudentAuth : BaseActivity() {
    private var studentIdIpt: TextInputEditText? = null
    private var studentPwdIpt: TextInputEditText? = null
    private var tm: TelephonyManager? = null
    private var btnFpwd: TextView? = null
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var saveButton: Button? = null
    var PRIVATE_MODE = 0

    private var disposable: Disposable? = null
    private lateinit var binding: ActivitySigninBinding

    @SuppressLint("NewApi", "StringFormatInvalid")
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
        btnFpwd = btnForgotPassword

        val window: Window = this@StudentAuth.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@StudentAuth, R.color.MMPrimary)

        saveButton!!.setOnClickListener { _ -> saveData() }

        btnFpwd!!.setOnClickListener { _ ->
            startActivity(Intent(this@StudentAuth, ForgotPasswordActivity::class.java))
        }

        if (ApiClient.getInstance(this).isFingerAuth()) {
            button_finger_login.visibility = View.VISIBLE
            val biometricManager: androidx.biometric.BiometricManager = androidx.biometric.BiometricManager.from(this)
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS ->
                Toast.makeText(applicationContext, resources.getString(R.string.finger_string_success), Toast.LENGTH_SHORT)
                    .show()
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Toast.makeText(applicationContext, resources.getString(R.string.finger_string_no_hardware), Toast.LENGTH_SHORT)
                        .show()
                    button_finger_login.visibility = View.GONE
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Toast.makeText(applicationContext, resources.getString(R.string.finger_string_hw_unavailable), Toast.LENGTH_SHORT)
                        .show()
                    button_finger_login.visibility = View.GONE
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    Toast.makeText(applicationContext, resources.getString(R.string.finger_string_none_enrolled), Toast.LENGTH_SHORT)
                        .show()
                    button_finger_login.visibility = View.GONE
                }
            }
            val executor: Executor = ContextCompat.getMainExecutor(this)
            val biometricPrompt = BiometricPrompt(
                this@StudentAuth,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(
                        errorCode: Int,
                        @NonNull errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                    }

                    override fun onAuthenticationSucceeded(@NonNull result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        requestStudentInfo()
                    }

                    override fun onAuthenticationFailed() {
                        Toast.makeText(applicationContext, resources.getString(R.string.fingerprint_failed_to_login), Toast.LENGTH_SHORT)
                            .show()
                        super.onAuthenticationFailed()
                    }
                })
            val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(resources.getString(R.string.loginTxt))
                .setDescription(resources.getString(R.string.use_your_fingerprint))
                .setNegativeButtonText(resources.getString(R.string.login_cancel))
                .build()
            button_finger_login!!.setOnClickListener { _ ->
                biometricPrompt.authenticate(promptInfo)
            }
        }
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

    fun requestStudentInfo () {
        showProgressDialog()
        this.inputControl(false)
        val prefs = securePrefs(this)
        if (ApiClient.getInstance(this).isReady()) {
            disposable = ApiClient.getInstance(this).requestInfo(prefs.getString(PreferenceConstants.token, null)!!)
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
                                result.error.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            this.inputControl(true)
                        }
                        hideProgressDialog()
                    },
                    { _ ->
                        hideProgressDialog()
                        Toast.makeText(
                            this@StudentAuth,
                            resources.getString(R.string.error_fallback_msg),
                            Toast.LENGTH_SHORT
                        ).show()
                        this.inputControl(true)
                    }
                )
        }
    }

    private fun inputControl (isEnabled: Boolean) {
        binding.studentIdIpt.isEnabled = isEnabled
        binding.studentPwdIpt.isEnabled = isEnabled
    }

    private fun LoginNow(sid: String?, spd: String?) {
        this.inputControl(false)
        val prefs = securePrefs(this)
        if (ApiClient.getInstance(this).isReady()) {
            disposable = ApiClient.getInstance(this).postLogin(sid!!, spd!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        if (result.error != true) {
                            if (ApiClient.getInstance(this).getToken() != result.token)
                                prefs[PreferenceConstants.fingerLoginAccount] = false
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
                            this.inputControl(true)
                        }
                        hideProgressDialog()
                    },
                    { _ ->
                        hideProgressDialog()
                        Toast.makeText(
                            this@StudentAuth,
                            resources.getString(R.string.error_fallback_msg),
                            Toast.LENGTH_SHORT
                        ).show()
                        this.inputControl(true)
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
