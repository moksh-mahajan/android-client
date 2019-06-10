//package com.mifos.mifosxdroid.online.checkerinbox
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.ImageView
//import android.widget.ListAdapter
//import android.widget.TextView
//import com.mifos.mifosxdroid.R
//import kotlinx.android.synthetic.main.layout_item_checker_inbox_and_tasks.view.*
//
//class PendingTasksListAdapter (var mCtx : Context, var resource : Int,
//                               var items : List<PendingTaskModel>)
//    : ArrayAdapter<PendingTaskModel>(mCtx, resource, items) {
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val layoutInflater = LayoutInflater.from(mCtx)
//        val view = layoutInflater.inflate(resource, null)
//        val taskIcon = view.findViewById<ImageView>(R.id.iv_task_icon)
//        val tvTaskName = view.findViewById<TextView>(R.id.tv_task)
//        val tvBadgeCount = view.findViewById<TextView>(R.id.tv_badge_counter)
//
//        var mItems = items[position]
//        taskIcon.setImageDrawable(mCtx.resources.getDrawable(mItems.icon))
//        tvTaskName.text = mItems.taskName
//        tvBadgeCount.text = mItems.count.toString()
//
//        return view
//    }
//}