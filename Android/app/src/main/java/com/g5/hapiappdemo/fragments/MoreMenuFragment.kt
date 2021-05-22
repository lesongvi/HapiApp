package com.g5.hapiappdemo.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.g5.hapiappdemo.PreferenceConstants
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.activities.DonateActivity
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.auth.StudentAuth
import com.g5.hapiappdemo.auth.set
import com.g5.hapiappdemo.databinding.FragmentMoreMenuBinding
import com.g5.hapiappdemo.extensions.PreferenceHelper
import io.realm.Realm


class MoreMenuFragment : Fragment() {

    lateinit var binding: FragmentMoreMenuBinding
    private lateinit var realm: Realm
    private var pd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_more_menu, container, false
        )
        pd = ProgressDialog(requireContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logoutBtn = binding.logoutBtn
        val facebookBot = binding.facebookBot
        val donateBtn = binding.donateBtn
        val fingerSwh = binding.sw

        fingerSwh.isChecked = ApiClient.getInstance(requireContext()).isFingerAuth()

        logoutBtn.setOnClickListener {
            pd!!.setCancelable(false)
            pd!!.setMessage(resources.getString(R.string.logout_loading))
            pd!!.show()

            realm = Realm.getDefaultInstance()

            realm.executeTransaction { realm ->
                realm.deleteAll()
            }

            val prefs = PreferenceHelper.securePrefs(requireActivity().applicationContext)
            if (!ApiClient.getInstance(requireContext()).isFingerAuth())
                prefs[PreferenceConstants.token] = null
            prefs[PreferenceConstants.sid] = null
            prefs[PreferenceConstants.sname] = null
            prefs[PreferenceConstants.semail] = null
            prefs[PreferenceConstants.sdt1] = null
            prefs[PreferenceConstants.sdt2] = null
            prefs[PreferenceConstants.loggedIn] = false
            prefs[PreferenceConstants.avatar] = null
            this.requireActivity().run{
                pd!!.hide()
                pd!!.dismiss()
                this.startActivity(Intent(this, StudentAuth::class.java))
                finish()
            }
        }

        facebookBot.setOnClickListener{
            this.requireActivity().run{
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.messenger.com/t/104511845053731"))
                startActivity(browserIntent)
            }
        }

        donateBtn.setOnClickListener{
            this.requireActivity().run{
                this.startActivity(Intent(this, DonateActivity::class.java))
            }
        }

        fingerSwh.setOnCheckedChangeListener { _, isChecked ->
            val prefs = PreferenceHelper.securePrefs(requireContext())
            if (isChecked) {
                Toast.makeText(context, resources.getString(R.string.finger_2fa_activated), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, resources.getString(R.string.finger_2fa_deactivated), Toast.LENGTH_SHORT).show()
            }
            prefs[PreferenceConstants.fingerLoginAccount] = isChecked
        }
    }

    companion object {
        fun newInstance(): MoreMenuFragment {
            return MoreMenuFragment()
        }
    }
}