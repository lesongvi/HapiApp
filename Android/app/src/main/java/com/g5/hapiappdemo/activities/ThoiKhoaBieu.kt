package com.g5.hapiappdemo.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.adapter.semesterDAdapter
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.databinding.ActivityTkbBinding
import com.g5.hapiappdemo.json.*
import com.g5.hapiappdemo.realmobj.*
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.delete
import io.realm.kotlin.where
import java.util.*

class ThoiKhoaBieu : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var realm: Realm
    private var disposable: Disposable? = null
    private lateinit var binding: ActivityTkbBinding
    private var mRecyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var mRecyclerViewItems: ArrayList<SemesterScheDetail> = ArrayList()
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var currentSemesterId: String? = null;
    private var currentsemesterWeek: String? = null;
    private var shimmerFrameLayout: ShimmerFrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTkbBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        shimmerFrameLayout = binding.phoderlayout
        shimmerFrameLayout!!.startShimmer()

        realm = Realm.getDefaultInstance()

        mRecyclerView = binding.tkbListView
        swipeRefreshLayout = binding.swiperefresh;
        mRecyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager

        this.getSCacheData()
        this.getTkbSemester()

        val toolbar: Toolbar = findViewById(R.id.toolbar_custom)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.home_btn_tkb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        swipeRefreshLayout!!.setOnRefreshListener { this.getSemesterScheDetail(this.currentSemesterId!!, this.currentsemesterWeek!!) }

        val window: Window = this@ThoiKhoaBieu.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@ThoiKhoaBieu, R.color.MMPrimary)
    }

    private fun getSCacheData () {
        val ss = realm.where<semesterSObj>().findAll()
        if (ss.size != 0)
        {
            val selectorAvailable = ArrayList<String>(ss.size)
            for (semesterOpt in ss) {
                selectorAvailable.add("${semesterOpt.semesterDetail}")
            }

            this.initSemesterSpinner(selectorAvailable, ss as List<SemesterSche>, true)
        }
    }

    private fun getTkbSemester() {
        if (ApiClient.getInstance(this).isReady()) {
            disposable = ApiClient.getInstance(this).getScheduleSemester()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        realm.executeTransaction { realm ->
                            realm.delete<semesterSObj>()
                        }
                        if (result.isNotEmpty()) {
                            val selectorAvailable = ArrayList<String>(result.size)
                            result.forEach { data: SemesterSche ->
                                data.semesterDetail?.let { selectorAvailable.add(it) }
                                realm.executeTransaction { realm ->
                                    val ss = realm.createObject<semesterSObj>()
                                    ss.semesterId = data.semesterId
                                    ss.semesterDetail = data.semesterDetail
                                }
                            }

                            binding.tkbListView.visibility = View.GONE

                            this.initSemesterSpinner(selectorAvailable, result, false)
                        } else {
                            shimmerFrameLayout!!.stopShimmer()
                            shimmerFrameLayout!!.visibility = View.GONE
                            Toast.makeText(
                                this@ThoiKhoaBieu,
                                resources.getString(R.string.retrieve_data_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    { _ ->
                        shimmerFrameLayout!!.stopShimmer()
                        shimmerFrameLayout!!.visibility = View.GONE
                        if(!realm.isClosed) {
                            Toast.makeText(
                                this@ThoiKhoaBieu,
                                resources.getString(R.string.retrieve_data_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
        }
    }

    private fun initSemesterSpinner(selectorAvailable: ArrayList<String>, result: List<SemesterSche>, isCache: Boolean) {
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<String>(
                this,
                R.layout.spinner_item_selected,
                selectorAvailable
            )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        binding.semester.visibility = View.VISIBLE

        val spinner = findViewById<Spinner>(R.id.semester)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.tkbListView.visibility = View.GONE
                binding.empty.visibility = View.GONE
                shimmerFrameLayout = binding.phoderlayout
                shimmerFrameLayout!!.visibility = View.VISIBLE
                shimmerFrameLayout!!.startShimmer()
                if (!isCache)
                {
                    result[position].semesterId?.let { getSemesterScheW(it) }
                }
                else {
                    val ws = realm.where<semesterSWObj>().findAll()
                    if (ws.size != 0)
                    {
                        val selectorAvailable = ArrayList<String>(ws.size)
                        for (weekOpt in ws) {
                            selectorAvailable.add("Tuần ${weekOpt.sotuan}, ${weekOpt.ngaybd} - ${weekOpt.ngaykt}")
                        }

                        getSemesterScheWCache(selectorAvailable, ws as List<SemesterScheWeek>, true, "")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getSemesterScheWCache(selectorAvailable: ArrayList<String>, result: List<SemesterScheWeek>, isCache: Boolean, semesterId: String) {
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<String>(
                this,
                R.layout.spinner_item_selected,
                selectorAvailable
            )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.daydetail.visibility = View.VISIBLE

        val spinner = findViewById<Spinner>(R.id.daydetail)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.tkbListView.visibility = View.GONE
                binding.empty.visibility = View.GONE
                shimmerFrameLayout = binding.phoderlayout
                shimmerFrameLayout!!.visibility = View.VISIBLE
                shimmerFrameLayout!!.startShimmer()
                if (!isCache) {
                    getSemesterScheDetail(semesterId, result[position].tenxacdinh!!)
                }
                else {
                    renderSemesterDetailCache()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getSemesterScheW(semesterId: String) {
        disposable = ApiClient.getInstance(this).viewSemesterScheW(semesterId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    realm.executeTransaction { realm ->
                        realm.delete<semesterSWObj>()
                    }
                    if (result.isNotEmpty()) {
                        val selectorAvailable = ArrayList<String>(result.size)
                        result.forEach { data: SemesterScheWeek ->
                            selectorAvailable.add("Tuần ${data.sotuan}, ${data.ngaybd} - ${data.ngaykt}")
                            realm.executeTransaction { realm ->
                                val ss = realm.createObject<semesterSWObj>()
                                ss.ngaybd = data.ngaybd
                                ss.ngaykt = data.ngaykt
                                ss.sotuan = data.sotuan
                                ss.tenxacdinh = data.tenxacdinh
                                ss.unixtimebd = data.unixtimebd
                                ss.unixtimekt = data.unixtimekt
                            }
                        }

                        this.getSemesterScheWCache(selectorAvailable, result, false, semesterId)
                    } else {
                        binding.tkbListView.visibility = View.GONE
                        shimmerFrameLayout!!.stopShimmer()
                        shimmerFrameLayout!!.visibility = View.GONE
                        if(!realm.isClosed) {
                            Toast.makeText(
                                this@ThoiKhoaBieu,
                                resources.getString(R.string.retrieve_data_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                { _ ->
                    hideProgressDialog()
                    Toast.makeText(
                        this@ThoiKhoaBieu,
                        resources.getString(R.string.student_disabled_or_some_thing),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
    }

    private fun renderSemesterDetailCache() {
        val sdo = realm.where<semesterSDObj>().findAll()
        if (sdo.size != 0)
        {
            for (sdetail in sdo) {
                mRecyclerViewItems.add(SemesterScheDetail(sdetail.lop, sdetail.mon, sdetail.ngaybd, sdetail.ngaykt, sdetail.nhom, sdetail.phong, sdetail.sotiet, sdetail.thu, sdetail.tietbatdau, sdetail.tinchi, sdetail.giangvien, sdetail.un_c1, sdetail.un_c2))
            }

            shimmerFrameLayout!!.stopShimmer()
            shimmerFrameLayout!!.visibility = View.GONE
            binding.empty.visibility = View.GONE
            binding.tkbListView.visibility = View.VISIBLE

            adapter = semesterDAdapter(this, mRecyclerViewItems)
            mRecyclerView!!.adapter = adapter
        }
    }

    private fun getSemesterScheDetail(semesterId: String, semesterWeek: String) {
        currentSemesterId = semesterId;
        currentsemesterWeek = semesterWeek;
        disposable = ApiClient.getInstance(this).viewSpecificSche(semesterId, semesterWeek)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    realm.executeTransaction { realm ->
                        realm.delete<semesterSDObj>()
                    }
                    if (result.isNotEmpty()) {
                        mRecyclerViewItems.clear()
                        result.forEach { data: SemesterScheDetail ->
                            mRecyclerViewItems.add(data)
                            realm.executeTransaction { realm ->
                                val sdo = realm.createObject<semesterSDObj>()
                                sdo.lop = data.lop
                                sdo.mon = data.mon
                                sdo.ngaybd = data.ngaybd
                                sdo.ngaykt = data.ngaykt
                                sdo.nhom = data.nhom
                                sdo.phong = data.phong
                                sdo.sotiet = data.sotiet
                                sdo.thu = data.thu
                                sdo.tietbatdau = data.tietbatdau
                                sdo.tinchi = data.tinchi
                                sdo.giangvien = data.giangvien
                                sdo.un_c1 = data.un_c1
                                sdo.un_c2 = data.un_c2
                            }
                        }

                        shimmerFrameLayout!!.stopShimmer()
                        shimmerFrameLayout!!.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        binding.tkbListView.visibility = View.VISIBLE

                        adapter = semesterDAdapter(this, mRecyclerViewItems)
                        mRecyclerView!!.adapter = adapter
                    } else {
                        binding.tkbListView.visibility = View.GONE
                        shimmerFrameLayout!!.stopShimmer()
                        shimmerFrameLayout!!.visibility = View.GONE
                        binding.empty.visibility = View.VISIBLE
                    }
                    swipeRefreshLayout!!.isRefreshing = false;
                },
                { _ ->
                    binding.tkbListView.visibility = View.GONE
                    shimmerFrameLayout!!.stopShimmer()
                    shimmerFrameLayout!!.visibility = View.GONE
                    if(!realm.isClosed) {
                        Toast.makeText(
                            this@ThoiKhoaBieu,
                            resources.getString(R.string.retrieve_data_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    swipeRefreshLayout!!.isRefreshing = false;
                }
            )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                onBackPressed()
        }
        return true
    }


    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}