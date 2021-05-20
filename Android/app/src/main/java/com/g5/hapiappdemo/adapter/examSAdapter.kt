package com.g5.hapiappdemo.adapter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.json.ExamScheDetail
import com.g5.hapiappdemo.popup.DetailPopup

class examSAdapter constructor(context: Context?, recyclerViewItems: ArrayList<ExamScheDetail>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MENU_ITEM_VIEW_TYPE = 0

    private var mContext: Context? = null

    private var mExpandedPosition = -1

    private var mRecyclerViewItems: ArrayList<ExamScheDetail>? = null

    init {
        this.mContext = context
        this.mRecyclerViewItems = recyclerViewItems
    }

    class examSViewHolder internal constructor(binding: View) : RecyclerView.ViewHolder(
        binding
    ) {
        var examDay: TextView = binding.findViewById(R.id.examDay) as TextView
        var et: TextView = binding.findViewById(R.id.examTitle) as TextView
        var eb: TextView = binding.findViewById(R.id.examBody) as TextView
        var etm: TextView = binding.findViewById(R.id.examTime) as TextView

        fun setSubjectName(name: String?) {
            et.text = name
        }

        fun setBody(body: String?) {
            eb.text = body
        }

        fun setDay(day: String?) {
            examDay.text = day
        }

        fun setTime(phut: String?) {
            etm.text = "$phut phút"
        }

    }


    override fun getItemCount(): Int {
        return mRecyclerViewItems!!.size
    }


    override fun getItemViewType(position: Int): Int {
        return MENU_ITEM_VIEW_TYPE
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val menuItemLayoutView: View = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.exam_item, viewGroup, false
        )
        return examSViewHolder(menuItemLayoutView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pointViewItemHolder = holder as examSViewHolder
        val esd: ExamScheDetail = mRecyclerViewItems!![position]
        val isExpanded = position == mExpandedPosition
        pointViewItemHolder.eb.ellipsize = TextUtils.TruncateAt.END
        pointViewItemHolder.eb.maxLines = 1
        pointViewItemHolder.itemView.isActivated = isExpanded
        pointViewItemHolder.itemView.setOnClickListener {
            val intent = Intent(mContext, DetailPopup::class.java)
            intent.putExtra("popuptitle", "Xem chi tiết lịch thi")
            intent.putExtra("popuptext", "Tên môn: ${esd.tenmon}\nMã môn: ${esd.mamon}\nPhòng thi: ${esd.phong}\nNhóm thi: ${esd.nhomthi}\nTổ thi: ${esd.tothi}\nNgày thi: ${esd.ngaythi}\nGiờ bắt đầu: ${esd.giobd}\nSố phút: ${esd.sophut}\nPhòng thi: ${esd.phong}\nGhi chú: ${esd.ghichu}")
            intent.putExtra("popupbtn", "Xong")
            intent.putExtra("darkstatusbar", false)
            mContext!!.startActivity(intent)
        }
        pointViewItemHolder.setSubjectName("${esd.tenmon}")
        pointViewItemHolder.setBody(
            "Ngày: ${esd.ngaythi} tại phòng: ${esd.phong}"
        )
        pointViewItemHolder.setTime(esd.sophut)
        pointViewItemHolder.setDay(esd.giobd)
    }
}