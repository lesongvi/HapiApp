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
import com.g5.hapiappdemo.json.EvaluateList
import com.g5.hapiappdemo.popup.DetailPopup

class studentEAdapter constructor(context: Context?, recyclerViewItems: ArrayList<EvaluateList>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MENU_ITEM_VIEW_TYPE = 0

    private var mContext: Context? = null

    private var mExpandedPosition = -1

    private var mRecyclerViewItems: ArrayList<EvaluateList>? = null

    init {
        this.mContext = context
        this.mRecyclerViewItems = recyclerViewItems
    }

    class studentEViewHolder internal constructor(binding: View) : RecyclerView.ViewHolder(
        binding
    ) {
        var nh: TextView = binding.findViewById(R.id.evTitle) as TextView
        var nb: TextView = binding.findViewById(R.id.evBody) as TextView
        var nt: TextView = binding.findViewById(R.id.evRanking) as TextView
        var er: AppCompatImageView = binding.findViewById(R.id.evaluateRank) as AppCompatImageView

        fun setName(name: String?) {
            nh.text = name
        }

        fun setBody(body: String?) {
            nb.text = body
        }

        fun setEvaluateRank(evr: String?) {
            nt.text = "Loại $evr"
            if (evr.equals("Xuất sắc")) {
                er.setImageResource(R.drawable.ic_cup)
            } else if (evr.equals("Khá")) {
                er.setImageResource(R.drawable.ic_gjob)
            } else if (evr.equals("Trung bình")) {
                er.setImageResource(R.drawable.ic_damn_bro_you_bad_0_huhu)
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
            R.layout.evaluate_item, viewGroup, false
        )
        return studentEViewHolder(menuItemLayoutView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pointViewItemHolder = holder as studentEViewHolder
        val ep: EvaluateList = mRecyclerViewItems!![position]
        val isExpanded = position == mExpandedPosition
        pointViewItemHolder.nb.ellipsize = if (isExpanded) null else TextUtils.TruncateAt.END
        pointViewItemHolder.nb.maxLines = if (isExpanded) Int.MAX_VALUE else 1
        pointViewItemHolder.itemView.isActivated = isExpanded
        pointViewItemHolder.itemView.setOnClickListener {
            val intent = Intent(mContext, DetailPopup::class.java)
            intent.putExtra("popuptitle", "Xem chi tiết rèn luyện")
            intent.putExtra("popuptext", "Điểm cá nhân: ${ep.diem_ca_nhan}\nĐiểm lớp: ${ep.diem_lop}\nĐiểm khoa: ${ep.diem_khoa}\nMã đợt khảo sát: ${ep.dot_khao_sat_id}\nHọc kỳ: ${ep.hoc_ky}, ${ep.nam_hoc}\nMã phiếu: ${ep.phieu_danh_gia_id}\nXếp loại tổng: ${ep.xep_loai}\n")
            intent.putExtra("popupbtn", "Xong")
            intent.putExtra("darkstatusbar", false)
            mContext!!.startActivity(intent)
        }
        pointViewItemHolder.setName("Học kỳ ${ep.hoc_ky}, ${ep.nam_hoc}")
        pointViewItemHolder.setBody(
            "Điểm cá nhân: ${ep.diem_ca_nhan} | Điểm lớp: ${ep.diem_lop} | Điểm khoa: ${ep.diem_khoa}"
        )
        pointViewItemHolder.setEvaluateRank(ep.xep_loai)
    }
}