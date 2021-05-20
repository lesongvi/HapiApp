package com.g5.hapiappdemo.activities

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.g5.hapiappdemo.PreferenceConstants
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.extensions.PreferenceHelper

abstract class BaseActivity : AppCompatActivity() {
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val defPrefs = PreferenceHelper.defaultPrefs(this)
        /*if (defPrefs.getBoolean(PreferenceConstants.theme, false)) {
            if (packageManager.getActivityInfo(componentName, 0).theme == R.style.AppTheme_NoActionBar) {
                setTheme(R.style.AppTheme_Dark_NoActionBar)
            } else {
                setTheme(R.style.AppTheme_Dark)
            }
        }*/
    }

    fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.setMessage(resources.getString(R.string.loading_txt))
        }
        mProgressDialog!!.show()
    }

    fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    fun setProgressMessage(msg: String?) {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.setMessage(msg)
        }
    }

    /*fun checkConnection(): Boolean {
        return ConnectivityReceiver.isConnected()
    }*/
}