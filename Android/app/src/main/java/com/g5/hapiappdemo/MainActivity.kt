package com.g5.hapiappdemo

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.g5.hapiappdemo.activities.OnboardingActivity
import com.g5.hapiappdemo.auth.StudentAuth
import com.g5.hapiappdemo.databinding.ActivityMainBinding
import com.g5.hapiappdemo.extensions.PreferenceHelper
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.dialog_update.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    private var lastNavigationItemId: Int? = null
    private var currentCode = 0
    private var DialogOpened: Boolean = false
    private var dialog: Dialog? = null
    private var text_view_go_pro: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home_fragment, R.id.support_fragment, R.id.account_fragment, R.id.more_menu_fragment),
            binding.drawerLayout
        )

        this.checkVersionUpdate()

        val window: Window = this@MainActivity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.MMPrimary)

        val preferences = getSharedPreferences(
            resources.getString(R.string.sharedprefname),
            MODE_PRIVATE
        )

        if (!preferences.getBoolean(resources.getString(R.string.onboarding_value), false)) {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        this.initialize()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) ||
                super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

    private fun initialize() {
        val prefs = PreferenceHelper.securePrefs(this)

        if (!prefs.getBoolean(PreferenceConstants.loggedIn, false)) {
            val intent = Intent(this, StudentAuth::class.java)
            startActivity(intent)
            finish()
        }
    }

    // BE CAREFUL!! External API request!
    private fun checkVersionUpdate () {
        val queue: com.android.volley.RequestQueue? = Volley.newRequestQueue(this)
        var pInfo: PackageInfo = packageManager.getPackageInfo(applicationContext.packageName, 0)
        var fupdate: String?
        var nupdate: String?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            currentCode = pInfo.longVersionCode.toInt()
        } else {
            currentCode = pInfo.versionCode
        }
        val stringRequest =
            StringRequest(
                Request.Method.GET, resources.getString(R.string.vcheck_api_url),
                Response.Listener<String?> { response ->
                    try {
                        val jsonObj = JSONObject(response)
                        if (jsonObj.has("version")) {
                            fupdate = jsonObj.getString("versionCode")
                            nupdate = jsonObj.getString("version")
                            if (fupdate != null && fupdate!!.toInt() > currentCode && !DialogOpened) {
                                showUpdateDialog(nupdate!!)
                            }
                        }
                    } catch (e: JSONException) {
                        //Silent is Golden
                    }
                }, Response.ErrorListener {
                    //Silent is Golden
                })
        queue!!.add(stringRequest)
    }

    private fun showUpdateDialog(newVersionName: String) {
        this.dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setContentView(R.layout.dialog_update)
        text_view_go_pro = dialog!!.findViewById(R.id.text_view_go_pro) as TextView
        text_view_go_pro!!.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://files.maxmines.com/songvi/_______/hapiappdemo-$newVersionName.apk")
                )
            )
        }
        dialog!!.show()
        DialogOpened = true
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ARG_NAVIGATION_ITEM_ID, lastNavigationItemId ?: 0)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val ARG_NAVIGATION_ITEM_ID = "navItemId"
    }
}
