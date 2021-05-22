package com.g5.hapiappdemo.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.adapter.NotifyAdapter
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.databinding.ActivityNotificationBinding
import com.g5.hapiappdemo.json.NotificationList
import com.g5.hapiappdemo.realmobj.notificationObj
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.delete
import io.realm.kotlin.where
import java.util.ArrayList

class NotificationActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    var tabLayout: TabLayout? = null
    lateinit var realm: Realm
    private var disposable: Disposable? = null
    private lateinit var binding: ActivityNotificationBinding
    private var mRecyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var mRecyclerViewItems: ArrayList<NotificationList> = ArrayList()
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var shimmerFrameLayout: ShimmerFrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = findViewById(R.id.toolbar_custom)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.home_btn_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        shimmerFrameLayout = binding.phoderlayout
        shimmerFrameLayout!!.startShimmer()

        realm = Realm.getDefaultInstance()

        mRecyclerView = binding.tbListView
        swipeRefreshLayout = binding.swiperefresh;
        mRecyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager

        this.getCachedNotifications()
        this.getNotifications()

        swipeRefreshLayout!!.setOnRefreshListener { this.getNotifications() }

        val window: Window = this@NotificationActivity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@NotificationActivity, R.color.MMPrimary)
    }

    private fun getCachedNotifications() {
        val results = realm.where<notificationObj>().findAll()
        if (results.size != 0)
        {
            for (notify in results) {
                mRecyclerViewItems.add(NotificationList(notify.notification, notify.unixtime))
            }
            binding.tbListView.visibility = View.VISIBLE
            shimmerFrameLayout!!.stopShimmer()
            shimmerFrameLayout!!.visibility = View.GONE

            adapter = NotifyAdapter(this, mRecyclerViewItems)
            mRecyclerView!!.adapter = adapter
        }
    }

    private fun getNotifications () {
        disposable = ApiClient.getInstance(this).viewNotifications()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    swipeRefreshLayout!!.isRefreshing = false;
                    realm.executeTransaction { realm ->
                        realm.delete<notificationObj>()
                    }
                    if (result.isNotEmpty()) {
                        mRecyclerViewItems.clear()
                        result.forEach { data: NotificationList ->
                            mRecyclerViewItems.add(data)
                            realm.executeTransaction { realm ->
                                val notify = realm.createObject<notificationObj>()
                                notify.notification = data.notification
                                notify.unixtime = data.unixtime
                            }
                        }

                        binding.tbListView.visibility = View.VISIBLE
                        shimmerFrameLayout!!.stopShimmer()
                        shimmerFrameLayout!!.visibility = View.GONE

                        adapter = NotifyAdapter(this, mRecyclerViewItems)
                        mRecyclerView!!.adapter = adapter

                    } else {
                        binding.tbListView.visibility = View.GONE
                        shimmerFrameLayout!!.stopShimmer()
                        shimmerFrameLayout!!.visibility = View.GONE
                        binding.empty.visibility = View.VISIBLE
                    }
                    swipeRefreshLayout!!.isRefreshing = false;
                },
                { _ ->
                    shimmerFrameLayout!!.stopShimmer()
                    shimmerFrameLayout!!.visibility = View.GONE
                    if(!realm.isClosed) {
                        Toast.makeText(
                            this@NotificationActivity,
                            resources.getString(R.string.retrieve_data_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    swipeRefreshLayout!!.isRefreshing = false;
                }
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}