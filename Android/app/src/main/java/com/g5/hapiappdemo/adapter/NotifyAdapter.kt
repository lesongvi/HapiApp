package com.g5.hapiappdemo.adapter

import android.content.Context
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.json.NotificationList
import java.util.*
import kotlin.collections.ArrayList

class NotifyAdapter constructor(context: Context?, recyclerViewItems: ArrayList<NotificationList>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mContext: Context? = null

    private var mExpandedPosition = -1

    private var mRecyclerViewItems: ArrayList<NotificationList>? = null

    init {
        this.mContext = context
        this.mRecyclerViewItems = recyclerViewItems
    }

    class NotifyViewHolder internal constructor(binding: View) : RecyclerView.ViewHolder(
        binding
    ) {
        var nh: TextView = binding.findViewById(R.id.notifyTitle) as TextView
        var nb: TextView = binding.findViewById(R.id.notifyBody) as TextView
        var nt: TextView = binding.findViewById(R.id.notifyTime) as TextView

        fun setTitle(name: String?) {
            nh.text = name
        }

        fun setBody(body: String?) {
            nb.text = body!!.replace("\\n", "\n")
        }

        fun setTime(unix: Long?) {
            nt.text = getTime(unix!!)
        }

        private fun getTime(milliSeconds: Long): String? {
            val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = milliSeconds
            return DateFormat.format("hh:mm a", cal).toString()
        }

    }

    override fun getItemCount(): Int {
        return mRecyclerViewItems!!.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val menuItemLayoutView: View = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.notification_item, viewGroup, false
        )
        return NotifyViewHolder(menuItemLayoutView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val noteItemHolder: NotifyViewHolder = holder as NotifyViewHolder
        val notify: NotificationList = mRecyclerViewItems!![position]
        val isExpanded = position === mExpandedPosition
        noteItemHolder.nb.ellipsize = if (isExpanded) null else TextUtils.TruncateAt.END
        noteItemHolder.nb.maxLines = if (isExpanded) Int.MAX_VALUE else 1
        noteItemHolder.itemView.isActivated = isExpanded
        noteItemHolder.itemView.setOnClickListener(View.OnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            notifyDataSetChanged()
        })
        noteItemHolder.setTitle("Thông báo ngày ${getDate(notify.unixtime!!*1000)}")
        noteItemHolder.setBody(notify.notification)
        noteItemHolder.setTime(notify.unixtime*1000)
    }

    private fun getDate(milliSeconds: Long): String? {
        val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = milliSeconds
        return DateFormat.format("dd/MM/yyyy", cal).toString()
    }
}