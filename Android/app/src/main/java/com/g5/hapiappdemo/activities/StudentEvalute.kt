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
import com.g5.hapiappdemo.adapter.studentEAdapter
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.databinding.ActivitySevaluateBinding
import com.g5.hapiappdemo.json.EvaluateList
import com.g5.hapiappdemo.realmobj.evaluateObj
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.delete
import io.realm.kotlin.where

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
    private var shimmerFrameLayout: ShimmerFrameLayout? = null

    inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object : TypeToken<T>() {}.type)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySevaluateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        shimmerFrameLayout = binding.phoderlayout
        shimmerFrameLayout!!.startShimmer()

        realm = Realm.getDefaultInstance()

        mRecyclerView = binding.evaluateListView
        swipeRefreshLayout = binding.swiperefresh;
        mRecyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager

        this.getCacheEvaluate()
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

    private fun getCacheEvaluate () {
        val evo = realm.where<evaluateObj>().findAll()
        if (evo.size != 0)
        {
            for (evoi in evo) {
                mRecyclerViewItems.add(EvaluateList(evoi.diem_ca_nhan, evoi.diem_khoa, evoi.diem_lop, evoi.dot_khao_sat_id, evoi.hienThiDiemTong, evoi.hoc_ky, evoi.nam_hoc, evoi.ngay_bd_sinhvien, evoi.ngay_kt_sinhvien, evoi.phieu_danh_gia_id, evoi.trang_thai_duyet, evoi.xep_loai))
            }
            shimmerFrameLayout!!.stopShimmer()
            shimmerFrameLayout!!.visibility = View.GONE
            binding.evaluateListView.visibility = View.VISIBLE

            adapter = studentEAdapter(this, mRecyclerViewItems)
            mRecyclerView!!.adapter = adapter

        }
    }

    private fun initEvaluateList () {
        if (ApiClient.getInstance(this).isReady()) {
            disposable = ApiClient.getInstance(this).viewEvaluateList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        realm.executeTransaction { realm ->
                            realm.delete<evaluateObj>()
                        }
                        if (result.isNotEmpty()) {
                            binding.empty.visibility = View.GONE
                            binding.evaluateListView.visibility = View.VISIBLE
                            mRecyclerViewItems.clear()
                            result.forEach { data: EvaluateList ->
                                mRecyclerViewItems.add(data)
                                realm.executeTransaction { realm ->
                                    val evo = realm.createObject<evaluateObj>()
                                    evo.diem_ca_nhan = data.diem_ca_nhan
                                    evo.diem_khoa = data.diem_khoa
                                    evo.diem_lop = data.diem_lop
                                    evo.dot_khao_sat_id = data.dot_khao_sat_id
                                    evo.hienThiDiemTong = data.hienThiDiemTong
                                    evo.hoc_ky = data.hoc_ky
                                    evo.nam_hoc = data.nam_hoc
                                    evo.ngay_bd_sinhvien = data.ngay_bd_sinhvien
                                    evo.ngay_kt_sinhvien = data.ngay_kt_sinhvien
                                    evo.phieu_danh_gia_id = data.phieu_danh_gia_id
                                    evo.trang_thai_duyet = data.trang_thai_duyet
                                    evo.xep_loai = data.xep_loai
                                }
                            }
                            shimmerFrameLayout!!.stopShimmer()
                            shimmerFrameLayout!!.visibility = View.GONE
                            binding.evaluateListView.visibility = View.VISIBLE

                            adapter = studentEAdapter(this, mRecyclerViewItems)
                            mRecyclerView!!.adapter = adapter
                        } else {
                            binding.evaluateListView.visibility = View.GONE
                            shimmerFrameLayout!!.stopShimmer()
                            shimmerFrameLayout!!.visibility = View.GONE
                            binding.empty.visibility = View.VISIBLE
                            Toast.makeText(
                                this@StudentEvalute,
                                resources.getString(R.string.retrieve_data_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        swipeRefreshLayout!!.isRefreshing = false;
                    },
                    { error ->
                        binding.empty.visibility = View.VISIBLE
                        binding.evaluateListView.visibility = View.GONE
                        shimmerFrameLayout!!.stopShimmer()
                        shimmerFrameLayout!!.visibility = View.GONE
                        if(!realm.isClosed) {
                            Toast.makeText(
                                this@StudentEvalute,
                                resources.getString(R.string.student_ev_chckpoint),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}