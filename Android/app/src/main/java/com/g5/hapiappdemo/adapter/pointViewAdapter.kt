package com.g5.hapiappdemo.adapter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.json.PointJson
import com.g5.hapiappdemo.popup.DetailPopup


class pointViewAdapter constructor(context: Context?, recyclerViewItems: ArrayList<PointJson>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MENU_ITEM_VIEW_TYPE = 0

    private var mContext: Context? = null

    private var mExpandedPosition = -1

    private var mRecyclerViewItems: ArrayList<PointJson>? = null

    init {
        this.mContext = context
        this.mRecyclerViewItems = recyclerViewItems
    }

    class pointViewViewHolder internal constructor(binding: View) : RecyclerView.ViewHolder(
        binding
    ) {
        var pi: AppCompatImageView = binding.findViewById(R.id.pointIcon) as AppCompatImageView
        var ph: TextView = binding.findViewById(R.id.pointTitle) as TextView
        var pb: TextView = binding.findViewById(R.id.pointBody) as TextView
        var pt: TextView = binding.findViewById(R.id.pointTotal) as TextView

        fun setName(name: String?) {
            ph.text = name
        }

        fun setBody(body: String?) {
            pb.text = body
        }

        fun setTotalPoint4(tk4: String?) {
            try {
                val tk4d: Double = tk4?.toDouble() ?: 0.0
                // Dưới 2 sao mà còn cười?
                if (tk4d < 2) {
                    pi.setImageResource(R.drawable.ic_damn_bro_you_bad_0_huhu)
                } else if (tk4d > 2 && tk4d < 3) {
                    pi.setImageResource(R.drawable.ic_try_harder_bro_2_ye)
                } else if (tk4d > 3 && tk4d < 3.5) {
                    pi.setImageResource(R.drawable.ic_good_job_25_ye)
                } else {
                    pi.setImageResource(R.drawable.ic_success_35_ye)
                }
                pt.text = "Tổng kết $tk4d"
            } catch (e: Exception) {
                pi.setImageResource(R.drawable.ic_damn_bro_you_bad_0_huhu)
                pt.text = "Tổng kết 0.0"
            }
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
            R.layout.point_item, viewGroup, false
        )
        return pointViewViewHolder(menuItemLayoutView)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pointViewItemHolder = holder as pointViewViewHolder
        val ep: PointJson = mRecyclerViewItems!![position]
        val isExpanded = position == mExpandedPosition
        pointViewItemHolder.pb.ellipsize = TextUtils.TruncateAt.END
        pointViewItemHolder.pb.maxLines = 1
        pointViewItemHolder.itemView.isActivated = isExpanded
        pointViewItemHolder.itemView.setOnClickListener {
            /*
            * Có quá nhiều nội dung cần chi tiết
            * mExpandedPosition = if (isExpanded) -1 else position
            * notifyDataSetChanged()
            */
            val intent = Intent(mContext, DetailPopup::class.java)
            intent.putExtra("popuptitle", "Xem chi tiết điểm")
            intent.putExtra("popuptext", "Tên môn: ${ep.tenmon}\nMã môn học: ${ep.mamon}\nSố tín chỉ: ${ep.tinchi}\nPhần trăm kiểm tra: ${ep.ptkt}%\nPhần trăm thi: ${ep.ptthi}%\nĐiểm kiểm tra lần 1: ${ep.diemkt1}\nĐiểm kiểm tra lần 2: ${ep.diemkt2}\nĐiểm thi lần 1: ${ep.thil1}\nTổng kết (chữ): ${ep.tkch}\nTổng kết (thang điểm 4): ${ep.tk4}")
            intent.putExtra("popupbtn", "Tôi ổn")
            intent.putExtra("darkstatusbar", false)
            mContext!!.startActivity(intent)
        }
        pointViewItemHolder.setName("${ep.tenmon} (${ep.mamon})")
        pointViewItemHolder.setBody(
            "${ep.tinchi} tín chỉ, bạn đạt loại ${ep.tkch} (${ep.tk4?:0.0}/4.0)"
        )
        pointViewItemHolder.setTotalPoint4(ep.tk4)
    }
}