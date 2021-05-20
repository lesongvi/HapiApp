package com.g5.hapiappdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.databinding.FragmentAccountBinding


class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding

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
            R.layout.fragment_account, container, false
        )
        return binding.root
    }

    private fun initStudentData() {
        val uData = ApiClient.getInstance(activity!!.applicationContext)

        binding.studentName.text = uData.getStudentName()
        binding.studentId.text = uData.getStudentID()
        binding.studentEmail.text = uData.getStudentEmail()
        binding.studentSdt1.text = uData.getStudentP1()
        binding.studentSdt2.text = uData.getStudentP2()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initStudentData()
    }
}
