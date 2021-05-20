package com.g5.hapiappdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.g5.hapiappdemo.auth.StudentAuth
import com.g5.hapiappdemo.auth.set
import com.g5.hapiappdemo.databinding.ActivityMainBinding
import com.g5.hapiappdemo.databinding.NavHeadermainBinding
import com.g5.hapiappdemo.extensions.PreferenceHelper
import com.google.android.material.navigation.NavigationView
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.app_bar_main.view.*
import java.io.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    private var lastNavigationItemId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Realm.init(this)
        /*val realmConfiguration = RealmConfiguration.Builder()
            .name("hapi_data.realm").build()
        Realm.setDefaultConfiguration(realmConfiguration)*/

        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home_fragment, R.id.miner_fragment, R.id.account_fragment, R.id.more_menu_fragment),
            binding.drawerLayout
        )

        val window: Window = this@MainActivity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.MMPrimary)

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

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ARG_NAVIGATION_ITEM_ID, lastNavigationItemId ?: 0)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val ARG_NAVIGATION_ITEM_ID = "navItemId"
    }
}
