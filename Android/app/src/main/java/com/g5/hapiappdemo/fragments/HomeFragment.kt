package com.g5.hapiappdemo.fragments

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.activities.*
import com.g5.hapiappdemo.databinding.FragmentHomeBinding
import com.g5.hapiappdemo.helpers.PrefUtils
import com.github.guilhe.circularprogressview.CircularProgressView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import java.util.*


class HomeFragment : Fragment(), RewardedVideoAdListener {

    lateinit var binding: FragmentHomeBinding
    private var timeToStart = 0
    private var timer1: CountDownTimer? = null
    private var timerState: TimerState? = null
    private var cp: CircularProgressView? = null
    private var prefUtils: PrefUtils? = null
    private var btn_dnt: AppCompatButton? = null
    private var MAX_TIME: Int = 2
    private var isSVisible: Boolean = false

    private var mAdView: AdView? = null
    private var mRewardedVideoAd: RewardedVideoAd? = null
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        mRewardedVideoAd!!.rewardedVideoAdListener = this;
        if(!mRewardedVideoAd!!.isLoaded)
            loadRewardedVideoAd()

        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd!!.adUnitId = getString(R.string.admob_interstitial_main)

        mInterstitialAd!!.loadAd(AdRequest.Builder().build())

        prefUtils = PrefUtils(context);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn_tt = getView()!!.findViewById<CardView>(R.id.btn_thongtin)
        val btn_xd = getView()!!.findViewById<CardView>(R.id.btn_diem)
        val btn_lt = getView()!!.findViewById<CardView>(R.id.btn_lichthi)
        val btn_tkb = getView()!!.findViewById<CardView>(R.id.btn_thoikhoabieu)
        val btn_dgrl = getView()!!.findViewById<CardView>(R.id.btn_dgrl)
        btn_dnt = getView()!!.findViewById(R.id.donateBtn)
        cp = getView()!!.findViewById(R.id.progressBarCircle);


        btn_tt?.setOnClickListener{
            requireActivity().run{
                startActivity(Intent(this, NotificationActivity::class.java))
            }
        }

        btn_xd?.setOnClickListener{
            this.requireActivity().run{
                this.startActivity(Intent(this, XemDiem::class.java))
            }
        }

        btn_lt?.setOnClickListener{
            this.requireActivity().run{
                this.startActivity(Intent(this, LichThi::class.java))
            }
        }

        btn_tkb?.setOnClickListener{
            this.requireActivity().run{
                this.startActivity(Intent(this, ThoiKhoaBieu::class.java))
            }
        }

        btn_dgrl?.setOnClickListener{
            this.requireActivity().run{
                this.startActivity(Intent(this, StudentEvalute::class.java))
            }
        }

        if (mInterstitialAd?.isLoaded == false)
        {
            btn_dnt?.isEnabled = false
        }

        btn_dnt?.setOnClickListener{
            this.requireActivity().run{
                if (mInterstitialAd?.isLoaded == true)
                {
                    cp?.max = MAX_TIME
                    timeToStart = MAX_TIME
                    timer1?.cancel()
                    startTimer(MAX_TIME)
                    btn_dnt?.isEnabled = false;
                    timerState = TimerState.RUNNING
                } else {
                    btn_dnt?.text = resources.getString(R.string.donate)
                    Toast.makeText(context, resources.getString(R.string.please_wait_for_load), Toast.LENGTH_SHORT).show()
                }
            }
        }

        mInterstitialAd!!.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd!!.loadAd(AdRequest.Builder().build())
            }

            override fun onAdLoaded() {
                super.onAdLoaded()

                btn_dnt?.isEnabled = true
            }
        }
    }

    private fun updatingUI() {
        if (timerState === TimerState.RUNNING) {
            val timeString: String = java.lang.String.format(
                Locale.US, "%02dp%02dg",
                (timeToStart / 60), (timeToStart % 60)
            )
            val ss = SpannableString(timeString)
            ss.setSpan(RelativeSizeSpan(0.8f), 2, 3, 0)
            ss.setSpan(RelativeSizeSpan(0.8f), 5, 6, 0)
            btn_dnt?.text = ss
            btn_dnt?.isEnabled = false
            cp?.progress = (MAX_TIME - timeToStart).toFloat()
        } else {
            btn_dnt?.isEnabled = true
            if (timer1 != null) timer1!!.cancel()
            btn_dnt?.text = resources.getString(R.string.donate)
        }
    }

    private fun startTimer(sec: Int) {
        var sec = sec
        val t = sec
        sec *= 1000
        timer1 = object : CountDownTimer(sec.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeToStart -= 1
                updatingUI()
            }

            override fun onFinish() {
                timerState = TimerState.STOPPED
                if (isSVisible && mInterstitialAd?.isLoaded == true) {
                    mInterstitialAd?.show()
                }
                onTimerFinish()
                updatingUI()
                btn_dnt?.isEnabled = true
            }
        }.start()
    }

    private fun onTimerFinish() {
        timeToStart = MAX_TIME
        prefUtils?.startedTime = 0
        spyAction("adtime")
        updatingUI()
    }

    private fun spyAction(change: String) {
        // Silent is Golden
    }

    override fun onResume() {
        isSVisible = true
        if (mInterstitialAd?.isLoaded == false) mInterstitialAd!!.loadAd(AdRequest.Builder().build())
        mRewardedVideoAd!!.resume(context)
        initTimer()
        updatingUI()
        super.onResume()
    }

    private fun initTimer() {
        val startTime = prefUtils!!.startedTime.toLong()
        if (startTime > 0) {
            btn_dnt?.isEnabled = false
            MAX_TIME = prefUtils!!.maxTime
            cp!!.max = MAX_TIME
            timeToStart = (MAX_TIME - (getNow() - startTime)) as Int
            if (timeToStart <= 0) {
                timeToStart = MAX_TIME
                timerState = TimerState.STOPPED
                cp!!.progress = MAX_TIME.toFloat()
                onTimerFinish()
            } else {
                startTimer(timeToStart)
                timerState = TimerState.RUNNING
            }
        } else {
            timeToStart = MAX_TIME
            timerState = TimerState.STOPPED
            updatingUI()
        }
    }

    override fun onPause() {
        isSVisible = false
        mRewardedVideoAd!!.pause(context)
        btn_dnt?.isEnabled = false
        if (timerState === TimerState.RUNNING) {
            timer1!!.cancel()
            prefUtils!!.maxTime = MAX_TIME
        }
        super.onPause()
    }

    override fun onRewardedVideoAdLoaded() {
        btn_dnt?.isEnabled = true
    }

    override fun onRewardedVideoAdOpened() {}

    override fun onRewardedVideoStarted() {}

    override fun onRewardedVideoAdClosed() {
        updatingUI()
        loadRewardedVideoAd()
    }

    override fun onRewarded(reward: RewardItem?) {
        spyAction("adreward")
        Toast.makeText(context, resources.getString(R.string.thanks_for_your_support), Toast.LENGTH_SHORT).show()
    }

    private fun getNow(): Long {
        val rightnow = Calendar.getInstance()
        return rightnow.timeInMillis / 1000
    }

    override fun onRewardedVideoAdLeftApplication() {}

    override fun onRewardedVideoAdFailedToLoad(i: Int) {}

    override fun onRewardedVideoCompleted() {}

    private fun loadRewardedVideoAd() {
        mRewardedVideoAd?.loadAd(
            resources.getString(R.string.admob_rewarded),
            AdRequest.Builder().build()
        )
    }

    private enum class TimerState {
        STOPPED, RUNNING
    }
}
