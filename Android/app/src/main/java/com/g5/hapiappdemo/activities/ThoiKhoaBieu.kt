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
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.adapter.semesterDAdapter
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.databinding.ActivityTkbBinding
import com.g5.hapiappdemo.json.*
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTkbBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        showProgressDialog()

        realm = Realm.getDefaultInstance()

        mRecyclerView = binding.tkbListView
        swipeRefreshLayout = binding.swiperefresh;
        mRecyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager

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

    private fun getTkbSemester() {
        if (ApiClient.getInstance(this).isReady()) {
            disposable = ApiClient.getInstance(this).getScheduleSemester()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        if (result.isNotEmpty()) {
                            val selectorAvailable = ArrayList<String>(result.size)
                            result.forEach { data: SemesterSche ->
                                data.semesterDetail?.let { selectorAvailable.add(it) }
                            }
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
                                    showProgressDialog()
                                    result[position].semesterId?.let { getSemesterScheW(it) }
                                    binding.tkbListView.visibility = View.INVISIBLE
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                        } else {
                            hideProgressDialog()
                            Toast.makeText(
                                this@ThoiKhoaBieu,
                                resources.getString(R.string.retrieve_data_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    { error ->
                        hideProgressDialog()
                        Toast.makeText(
                            this@ThoiKhoaBieu,
                            resources.getString(R.string.retrieve_data_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
        }
    }

    private fun getSemesterScheW(semesterId: String) {
        disposable = ApiClient.getInstance(this).viewSemesterScheW(semesterId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    if (result.isNotEmpty()) {
                        val selectorAvailable = ArrayList<String>(result.size)
                        result.forEach { data: SemesterScheWeek ->
                            selectorAvailable.add("Tuần ${data.sotuan}, ${data.ngaybd} - ${data.ngaykt}");
                        }
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
                                showProgressDialog()
                                getSemesterScheDetail(semesterId, result[position].tenxacdinh!!)
                                binding.tkbListView.visibility = View.INVISIBLE
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                    } else {
                        hideProgressDialog()
                        Toast.makeText(
                            this@ThoiKhoaBieu,
                            resources.getString(R.string.retrieve_data_failed),
                            Toast.LENGTH_SHORT
                        ).show()
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

    private fun getSemesterScheDetail(semesterId: String, semesterWeek: String) {
        currentSemesterId = semesterId;
        currentsemesterWeek = semesterWeek;
        disposable = ApiClient.getInstance(this).viewSpecificSche(semesterId, semesterWeek)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    if (result.isNotEmpty()) {
                        mRecyclerViewItems.clear();
                        result.forEach { data: SemesterScheDetail ->
                            mRecyclerViewItems.add(data);
                        }

                        adapter = semesterDAdapter(this, mRecyclerViewItems)
                        mRecyclerView!!.adapter = adapter

                        hideProgressDialog()
                        binding.tkbListView.visibility = View.VISIBLE
                        binding.empty.visibility = View.INVISIBLE
                    } else {
                        hideProgressDialog()
                        binding.empty.visibility = View.VISIBLE
                    }
                    swipeRefreshLayout!!.isRefreshing = false;
                },
                { _ ->
                    hideProgressDialog()
                    Toast.makeText(
                        this@ThoiKhoaBieu,
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