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
import com.g5.hapiappdemo.adapter.examSAdapter
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.databinding.ActivityLichThiBinding
import com.g5.hapiappdemo.json.ExamScheDetail
import com.g5.hapiappdemo.realmobj.examSObj
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.delete
import io.realm.kotlin.where
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
    private var shimmerFrameLayout: ShimmerFrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLichThiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        shimmerFrameLayout = binding.phoderlayout
        shimmerFrameLayout!!.startShimmer()

        realm = Realm.getDefaultInstance()

        mRecyclerView = binding.ltListView
        swipeRefreshLayout = binding.swiperefresh;
        mRecyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager

        this.getCacheSPData()
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

    private fun getCacheSPData () {
        val esd = realm.where<examSObj>().findAll()
        if (esd.size != 0)
        {
            for (exsc in esd) {
                mRecyclerViewItems.add(ExamScheDetail(exsc.mamon, exsc.tenmon, exsc.nhomthi, exsc.tothi, exsc.ngaythi, exsc.giobd, exsc.sophut, exsc.phong, exsc.ghichu))
            }
            shimmerFrameLayout!!.stopShimmer()
            shimmerFrameLayout!!.visibility = View.GONE
            binding.empty.visibility = View.INVISIBLE
            binding.ltListView.visibility = View.VISIBLE

            adapter = examSAdapter(this, mRecyclerViewItems)
            mRecyclerView!!.adapter = adapter

        }
    }

    private fun getExamSche () {
        disposable = ApiClient.getInstance(this).viewExamSche()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    swipeRefreshLayout!!.isRefreshing = false
                    realm.executeTransaction { realm ->
                        realm.delete<examSObj>()
                    }
                    if (result.isNotEmpty()) {
                        mRecyclerViewItems.clear()
                        result.forEach { data: ExamScheDetail ->
                            mRecyclerViewItems.add(data)
                            realm.executeTransaction { realm ->
                                val eso = realm.createObject<examSObj>()
                                eso.mamon = data.mamon
                                eso.tenmon = data.tenmon
                                eso.nhomthi = data.nhomthi
                                eso.tothi = data.tothi
                                eso.ngaythi = data.ngaythi
                                eso.giobd = data.giobd
                                eso.sophut = data.sophut
                                eso.phong = data.phong
                                eso.ghichu = data.ghichu
                            }
                        }

                        binding.empty.visibility = View.INVISIBLE
                        binding.ltListView.visibility = View.VISIBLE
                        adapter = examSAdapter(this, mRecyclerViewItems)
                        mRecyclerView!!.adapter = adapter

                        shimmerFrameLayout!!.stopShimmer()
                        shimmerFrameLayout!!.visibility = View.GONE

                    } else {
                        binding.ltListView.visibility = View.INVISIBLE
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
                            this@LichThi,
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