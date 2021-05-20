package com.g5.hapiappdemo.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.g5.hapiappdemo.PreferenceConstants
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.auth.StudentAuth
import com.g5.hapiappdemo.auth.set
import com.g5.hapiappdemo.databinding.FragmentMoreMenuBinding
import com.g5.hapiappdemo.extensions.PreferenceHelper

class MoreMenuFragment : Fragment() {

    lateinit var binding: FragmentMoreMenuBinding

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logoutBtn = binding.logoutBtn
        val facebookBot = binding.facebookBot
        val donateBtn = binding.donateBtn

        logoutBtn.setOnClickListener{
            val prefs = PreferenceHelper.securePrefs(activity!!.applicationContext)
            prefs[PreferenceConstants.sid] = null
            prefs[PreferenceConstants.sname] = null
            prefs[PreferenceConstants.token] = null
            prefs[PreferenceConstants.semail] = null
            prefs[PreferenceConstants.sdt1] = null
            prefs[PreferenceConstants.sdt2] = null
            prefs[PreferenceConstants.loggedIn] = false
            this.requireActivity().run{
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
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://donate.rqn9.com"))
                startActivity(browserIntent)
            }
        }
    }
}