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
import com.g5.hapiappdemo.adapter.pointViewAdapter
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.databinding.ActivityXemDiemBinding
import com.g5.hapiappdemo.json.PointJson
import com.g5.hapiappdemo.json.SemesterPoint
import com.g5.hapiappdemo.realmobj.jsonPObj
import com.g5.hapiappdemo.realmobj.semesterPObj
import com.g5.hapiappdemo.realmobj.studentPoint
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


class XemDiem : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var realm: Realm
    private var disposable: Disposable? = null
    private lateinit var binding: ActivityXemDiemBinding
    private var mRecyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var mRecyclerViewItems: ArrayList<PointJson> = ArrayList()
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var currentSemesterId: String? = null
    private var currentSemesterYear: String? = null
    lateinit var list: Sequence<studentPoint>
    var gson = Gson()
    private var shimmerFrameLayout: ShimmerFrameLayout? = null

    inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object : TypeToken<T>() {}.type)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityXemDiemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        shimmerFrameLayout = binding.phoderlayout
        shimmerFrameLayout!!.startShimmer()

        realm = Realm.getDefaultInstance()

        mRecyclerView = binding.pointListView
        swipeRefreshLayout = binding.swiperefresh;
        mRecyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager

        this.getCacheData()
        this.initPointData()

        val lm = LinearLayoutManager(this)
        lm.reverseLayout = true
        lm.stackFromEnd = true
        mRecyclerView!!.layoutManager = lm

        val toolbar: Toolbar = findViewById(R.id.toolbar_custom)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.home_btn_xd)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        swipeRefreshLayout!!.setOnRefreshListener { this.getSemesterPointData(currentSemesterId, currentSemesterYear) }

        val window: Window = this@XemDiem.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@XemDiem, R.color.MMPrimary)
    }

    private fun getCacheData () {
        val spoint = realm.where<semesterPObj>().findAll()
        if (spoint.size != 0)
        {
            val selectorAvailable = ArrayList<String>(spoint.size)
            for (notify in spoint) {
                selectorAvailable.add("Học kỳ ${notify.hocky}, năm học ${notify.namhoc}")
            }

            this.initPointSpinner(selectorAvailable, spoint as List<SemesterPoint>, true)
        }
    }

    private fun getCacheSPData () {
        val spdetail = realm.where<jsonPObj>().findAll()
        if (spdetail.size != 0)
        {
            for (notify in spdetail) {
                mRecyclerViewItems.add(PointJson(notify.mamon, notify.tenmon, notify.tinchi, notify.ptkt, notify.ptthi, notify.diemkt1, notify.diemkt2, notify.thil1, notify.tkch, notify.tk4))
            }

            binding.empty.visibility = View.GONE
            shimmerFrameLayout!!.stopShimmer()
            shimmerFrameLayout!!.visibility = View.GONE
            binding.pointListView.visibility = View.VISIBLE

            adapter = pointViewAdapter(this, mRecyclerViewItems)
            mRecyclerView!!.adapter = adapter
        }
    }

    private fun initPointData () {
        if (ApiClient.getInstance(this).isReady()) {
            disposable = ApiClient.getInstance(this).viewSemesterPoint()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        realm.executeTransaction { realm ->
                            realm.delete<semesterPObj>()
                        }
                        if (result.isNotEmpty()) {
                            val selectorAvailable = ArrayList<String>(result.size)
                            result.forEach { data: SemesterPoint ->
                                selectorAvailable.add("Học kỳ ${data.hocky}, năm học ${data.namhoc}")
                                realm.executeTransaction { realm ->
                                    val spoint = realm.createObject<semesterPObj>()
                                        spoint.hocky = data.hocky
                                        spoint.namhoc = data.namhoc
                                }
                            }

                            this.initPointSpinner(selectorAvailable, result, false)
                        } else {
                            if(!realm.isClosed) {
                                Toast.makeText(
                                    this@XemDiem,
                                    resources.getString(R.string.retrieve_data_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    { _ ->
                        if(!realm.isClosed) {
                            Toast.makeText(
                                this@XemDiem,
                                resources.getString(R.string.retrieve_data_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
        }
    }

    private fun initPointSpinner(selectorAvailable: ArrayList<String>, result: List<SemesterPoint>, isCache: Boolean) {
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<String>(
                this,
                R.layout.spinner_item_selected,
                selectorAvailable
            )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        binding.semesterP.visibility = View.VISIBLE

        val spinner = findViewById<Spinner>(R.id.semester_p)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.pointListView.visibility = View.GONE
                binding.phoderlayout.visibility = View.VISIBLE
                shimmerFrameLayout = binding.phoderlayout
                shimmerFrameLayout!!.startShimmer()
                if (!isCache)
                    getSemesterPointData(result[position].hocky, result[position].namhoc)
                else
                    getCacheSPData()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getSemesterPointData (hocky: String?, namhoc: String?) {
        currentSemesterId = hocky;
        currentSemesterYear = namhoc;
        disposable = ApiClient.getInstance(this).viewSpecificPoint(hocky, namhoc)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    realm.executeTransaction { realm ->
                        realm.delete<jsonPObj>()
                    }
                    if (result.isNotEmpty()) {
                        mRecyclerViewItems.clear()
                        result.forEach { data: PointJson ->
                            mRecyclerViewItems.add(data)
                            realm.executeTransaction { realm ->
                                val diem = realm.createObject<jsonPObj>()
                                diem.mamon = data.mamon
                                diem.tenmon = data.tenmon
                                diem.tinchi = data.tinchi
                                diem.ptkt = data.ptkt
                                diem.ptthi = data.ptthi
                                diem.diemkt1 = data.diemkt1
                                diem.diemkt2 = data.diemkt2
                                diem.thil1 = data.thil1
                                diem.tkch = data.tkch
                                diem.tk4 = data.tk4
                            }
                        }

                        binding.empty.visibility = View.GONE
                        shimmerFrameLayout!!.stopShimmer()
                        shimmerFrameLayout!!.visibility = View.GONE
                        binding.pointListView.visibility = View.VISIBLE

                        adapter = pointViewAdapter(this, mRecyclerViewItems)
                        mRecyclerView!!.adapter = adapter

                    } else {
                        binding.pointListView.visibility = View.GONE
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
                            this@XemDiem,
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

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}