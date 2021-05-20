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
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.adapter.examSAdapter
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.databinding.ActivityLichThiBinding
import com.g5.hapiappdemo.json.ExamScheDetail
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.*


class LichThi : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    var tabLayout: TabLayout? = null
    lateinit var realm: Realm
    private var disposable: Disposable? = null
    private lateinit var binding: ActivityLichThiBinding
    private var mRecyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var mRecyclerViewItems: ArrayList<ExamScheDetail> = ArrayList()
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLichThiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        showProgressDialog()

        realm = Realm.getDefaultInstance()

        mRecyclerView = binding.ltListView
        swipeRefreshLayout = binding.swiperefresh;
        mRecyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager

        this.getExamSche()

        val toolbar: Toolbar = findViewById(R.id.toolbar_custom)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.home_btn_lt)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        swipeRefreshLayout!!.setOnRefreshListener { this.getExamSche() }

        val window: Window = this@LichThi.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@LichThi, R.color.MMPrimary)
    }

    private fun getExamSche () {
        disposable = ApiClient.getInstance(this).viewExamSche()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    swipeRefreshLayout!!.isRefreshing = false;
                    if (result.isNotEmpty()) {
                        mRecyclerViewItems.clear();
                        result.forEach { data: ExamScheDetail ->
                            mRecyclerViewItems.add(data);
                        }

                        adapter = examSAdapter(this, mRecyclerViewItems)
                        mRecyclerView!!.adapter = adapter

                        hideProgressDialog()

                    } else {
                        hideProgressDialog()
                        binding.empty.visibility = View.VISIBLE
                    }
                    swipeRefreshLayout!!.isRefreshing = false;
                },
                { _ ->
                    hideProgressDialog()
                    Toast.makeText(
                        this@LichThi,
                        resources.getString(R.string.retrieve_data_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    swipeRefreshLayout!!.isRefreshing = false;
                }
            )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}