package com.g5.hapiappdemo.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.adapter.studentEAdapter
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.databinding.ActivitySevaluateBinding
import com.g5.hapiappdemo.json.EvaluateList
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm

class StudentEvalute : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var realm: Realm
    private var disposable: Disposable? = null
    private lateinit var binding: ActivitySevaluateBinding
    private var mRecyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var mRecyclerViewItems: ArrayList<EvaluateList> = ArrayList()
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    lateinit var list: Sequence<EvaluateList>
    var gson = Gson()

    inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object : TypeToken<T>() {}.type)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySevaluateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        showProgressDialog()

        realm = Realm.getDefaultInstance()

        mRecyclerView = binding.evaluateListView
        swipeRefreshLayout = binding.swiperefresh;
        mRecyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager

        this.initEvaluateList()

        val lm = LinearLayoutManager(this)
        lm.reverseLayout = true
        lm.stackFromEnd = true
        mRecyclerView!!.layoutManager = lm

        val toolbar: Toolbar = findViewById(R.id.toolbar_custom)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.home_btn_dgrl)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        swipeRefreshLayout!!.setOnRefreshListener { this.initEvaluateList() }

        val window: Window = this@StudentEvalute.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@StudentEvalute, R.color.MMPrimary)
    }

    private fun initEvaluateList () {
        if (ApiClient.getInstance(this).isReady()) {
            disposable = ApiClient.getInstance(this).viewEvaluateList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        if (result.isNotEmpty()) {
                            binding.empty.visibility = View.INVISIBLE
                            binding.evaluateListView.visibility = View.VISIBLE
                            mRecyclerViewItems.clear()
                            result.forEach { data: EvaluateList ->
                                mRecyclerViewItems.add(data)
                            }

                            adapter = studentEAdapter(this, mRecyclerViewItems)
                            mRecyclerView!!.adapter = adapter

                            hideProgressDialog()
                        } else {
                            hideProgressDialog()
                            Toast.makeText(
                                this@StudentEvalute,
                                resources.getString(R.string.retrieve_data_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.empty.visibility = View.VISIBLE
                            binding.evaluateListView.visibility = View.INVISIBLE
                        }
                        swipeRefreshLayout!!.isRefreshing = false;
                    },
                    { _ ->
                        binding.empty.visibility = View.VISIBLE
                        binding.evaluateListView.visibility = View.INVISIBLE
                        hideProgressDialog()
                        Toast.makeText(
                            this@StudentEvalute,
                            resources.getString(R.string.student_ev_chckpoint),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}