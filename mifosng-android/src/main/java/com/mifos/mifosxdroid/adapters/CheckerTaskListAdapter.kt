package com.mifos.mifosxdroid.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mifos.mifosxdroid.R
import com.mifos.objects.CheckerTask
import kotlinx.android.synthetic.main.item_checker_task.view.*

class CheckerTaskListAdapter(var items: List<CheckerTask>, var context: Context)
    : RecyclerView.Adapter<CheckerTaskListAdapter.ViewHolder>(){

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checker_task, parent,
                false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tvCheckerTaskId.text = items.get(position).id.toString()
        holder?.tvCheckerTaskDate.text = items.get(position).madeOnDate
        holder?.tvCheckerTaskStatus.text = items.get(position).status
        holder?.tvCheckerTaskAction.text = items.get(position).action
        holder?.tvCheckerTaskEntity.text = items.get(position).entity
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val tvCheckerTaskId = view.tv_checker_task_id
        val tvCheckerTaskDate = view.tv_checker_task_date
        val tvCheckerTaskStatus = view.tv_checker_task_status
        val tvCheckerTaskAction = view.tv_checker_task_action
        val tvCheckerTaskEntity = view.tv_checker_task_entity
    }
}

