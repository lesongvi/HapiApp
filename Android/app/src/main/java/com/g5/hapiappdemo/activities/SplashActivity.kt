package com.g5.hapiappdemo.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.g5.hapiappdemo.MainActivity
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.databinding.ActivitySplashBinding
import io.realm.Realm
import io.realm.RealmConfiguration


class SplashActivity : AppCompatActivity() {
    var splashProgress: ProgressBar? = null
    var SPLASH_TIME = 1000
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val window: Window = this@SplashActivity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@SplashActivity, R.color.MMPrimary)

        splashProgress = findViewById(R.id.splashProgress)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            splashProgress!!.progressDrawable.colorFilter =
                BlendModeColorFilter(Color.BLACK, BlendMode.SRC_IN)
        } else {
            splashProgress!!.progressDrawable.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        }
        splashProgress!!.scaleY = 3f
        splashProgress!!.visibility = View.GONE
        playProgress()

        Handler().postDelayed(Runnable {
            val mySuperIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(mySuperIntent)
            finish()
        }, SPLASH_TIME.toLong())

        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .name("hapi_data.realm").build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    private fun playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
            .setDuration(5000)
            .start()
    }
}