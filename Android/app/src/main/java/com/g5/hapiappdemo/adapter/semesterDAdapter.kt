package com.g5.hapiappdemo.adapter

import com.g5.hapiappdemo.json.SemesterScheDetail
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.popup.DetailPopup

class semesterDAdapter constructor(context: Context?, recyclerViewItems: ArrayList<SemesterScheDetail>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MENU_ITEM_VIEW_TYPE = 0

    private var mContext: Context? = null

    private var mExpandedPosition = -1

    private var mRecyclerViewItems: ArrayList<SemesterScheDetail>? = null

    init {
        this.mContext = context
        this.mRecyclerViewItems = recyclerViewItems
    }

    class semesterDViewHolder internal constructor(binding: View) : RecyclerView.ViewHolder(
        binding
    ) {
        var tkbThu: TextView = binding.findViewById(R.id.tkbDay) as TextView
        var sh: TextView = binding.findViewById(R.id.semesterTitle) as TextView
        var sl: TextView = binding.findViewById(R.id.semesterLocation) as TextView
        var st: TextView = binding.findViewById(R.id.semesterTime) as TextView
        var sc: TextView = binding.findViewById(R.id.semesterClass) as TextView

        fun setName(name: String?) {
            sh.text = name
        }

        fun setLocation(location: String?) {
            sl.text = "Phòng: ${location}"
        }

        fun setClass(sclass: String?) {
            sc.text = "Lớp: ${sclass}"
        }

        fun setDay(day: String?) {
            tkbThu.text = day
        }

        fun setTime(tk4: String?) {
            st.text = tk4
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
            R.layout.tkb_item, viewGroup, false
        )
        return semesterDViewHolder(menuItemLayoutView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pointViewItemHolder = holder as semesterDViewHolder
        val ep: SemesterScheDetail = mRecyclerViewItems!![position]
        val isExpanded = position == mExpandedPosition
        pointViewItemHolder.itemView.isActivated = isExpanded
        pointViewItemHolder.itemView.setOnClickListener {
            val intent = Intent(mContext, DetailPopup::class.java)
            intent.putExtra("popuptitle", "Xem chi tiết môn")
            intent.putExtra("popuptext", "Tên môn: ${ep.mon}\nLớp: ${ep.lop}\nPhòng: ${ep.phong}\nSố tín chỉ: ${ep.tinchi}\nSố tiết: ${ep.sotiet}\nTiết bắt đầu: ${ep.tietbatdau}\nTên giảng viên: ${ep.giangvien}\nNgày bắt đầu học: ${ep.ngaybd}\nNgày kết thúc học: ${ep.ngaykt}")
            intent.putExtra("popupbtn", "Xong")
            intent.putExtra("darkstatusbar", false)
            mContext!!.startActivity(intent)
        }
        pointViewItemHolder.setName("${ep.mon}")
        pointViewItemHolder.setLocation(ep.phong)
        pointViewItemHolder.setClass(ep.lop)
        pointViewItemHolder.setDay(ep.thu)
        pointViewItemHolder.setTime(ep.tietbatdau)
    }
}