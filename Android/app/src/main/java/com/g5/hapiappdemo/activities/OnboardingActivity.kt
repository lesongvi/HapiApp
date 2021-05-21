package com.g5.hapiappdemo.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.g5.hapiappdemo.MainActivity
import com.g5.hapiappdemo.R
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment


class OnboardingActivity: AppIntro() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window: Window = this@OnboardingActivity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@OnboardingActivity, R.color.MMPrimary)

        addSlide(
            AppIntroFragment
                .newInstance(
                    getString(R.string.ob_first_title),
                    getString(R.string.welcome_to_hapiapp),
                    R.drawable.user,
                    R.color.white,
                    0,
                    0,
                    0,
                    0,
                    R.drawable.tut_bg
                )
        )
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.ob_announcement_title),
                getString(R.string.ob_announcement_desc),
                R.drawable.ic_education,
                R.color.black,
                0,
                0,
                0,
                0,
                R.drawable.tut_bg2
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.ob_point_title),
                getString(R.string.ob_point_desc),
                R.drawable.ic_grade,
                R.color.black,
                0,
                0,
                0,
                0,
                R.drawable.tut_bg3
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.ob_ev_title),
                getString(R.string.ob_ev_desc),
                R.drawable.ic_lich_thi,
                R.color.black,
                0,
                0,
                0,
                0,
                R.drawable.tut_bg4
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.ob_schedule_title),
                getString(R.string.ob_schedule_desc),
                R.drawable.ic_tkb,
                R.color.black,
                0,
                0,
                0,
                0,
                R.drawable.tut_bg5
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.ob_evaluate_title),
                getString(R.string.ob_evaluate_desc),
                R.drawable.ic_train,
                R.color.black,
                0,
                0,
                0,
                0,
                R.drawable.tut_bg6
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.ob_support_title),
                getString(R.string.ob_support_desc),
                R.drawable.welcome_to_hapi,
                R.color.black,
                0,
                0,
                0,
                0,
                R.drawable.tut_bg7
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.permission_title),
                getString(R.string.permission_detail),
                R.drawable.ic_account,
                R.color.black,
                0,
                0,
                0,
                0,
                R.drawable.tut_bg8
            )
        )
        askForPermissions(
            arrayOf<String>(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                Manifest.permission.ACCESS_NETWORK_STATE
            ),
            8,
            true
        )

        setSkipText(resources.getString(R.string.ob_skip))
        setDoneText(resources.getString(R.string.ob_done))
    }

    private fun resize(image: Drawable): Drawable? {
        val b = (image as BitmapDrawable).bitmap
        val bitmapResized = Bitmap.createScaledBitmap(b, 50, 50, false)
        return BitmapDrawable(resources, bitmapResized)
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)

        val sharedPreferencesEditor: SharedPreferences.Editor =
            getSharedPreferences(resources.getString(R.string.sharedprefname), MODE_PRIVATE).edit()
        sharedPreferencesEditor.putBoolean(
            resources.getString(R.string.onboarding_value), true
        )
        sharedPreferencesEditor.apply()
        startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val sharedPreferencesEditor: SharedPreferences.Editor =
            getSharedPreferences(resources.getString(R.string.sharedprefname), MODE_PRIVATE).edit()
        sharedPreferencesEditor.putBoolean(
            resources.getString(R.string.onboarding_value), true
        )
        sharedPreferencesEditor.apply()
        startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
        finish()
    }

    override fun onUserDeniedPermission(permissionName: String) {
        Toast.makeText(
            this,
            resources.getString(R.string.ondeniedpermission),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onUserDisabledPermission(permissionName: String) {
        Toast.makeText(
            this,
            resources.getString(R.string.ondisabledpermission),
            Toast.LENGTH_LONG
        ).show()
    }
}