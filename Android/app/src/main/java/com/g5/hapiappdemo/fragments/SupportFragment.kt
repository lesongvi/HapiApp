package com.g5.hapiappdemo.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.databinding.FragmentSupportBinding

class SupportFragment : Fragment() {

    lateinit var binding: FragmentSupportBinding

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
            R.layout.fragment_support, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sp1 = binding.supportCard1
        val sp2 = binding.supportCard2
        val intent = Intent(Intent.ACTION_SENDTO)

        intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.support_need_help))
        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.support_need_help_body))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        sp1.setOnClickListener{
            this.requireActivity().run{
                intent.data =
                    Uri.parse(resources.getString(R.string.sp1_mail))
                startActivity(intent)
            }
        }

        sp2.setOnClickListener{
            this.requireActivity().run{
                intent.data =
                    Uri.parse(resources.getString(R.string.sp2_mail))
                startActivity(intent)
            }
        }
    }
}