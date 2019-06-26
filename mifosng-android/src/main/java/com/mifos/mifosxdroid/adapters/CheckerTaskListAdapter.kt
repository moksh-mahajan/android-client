package com.mifos.mifosxdroid.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mifos.mifosxdroid.R
import com.mifos.mifosxdroid.online.checkerinbox.CheckerInboxFragment
import com.mifos.objects.CheckerTask
import kotlinx.android.synthetic.main.item_checker_task.view.*

class CheckerTaskListAdapter(private var items: MutableList<CheckerTask>,
                             private var checkerInboxFragment: CheckerInboxFragment)
    : RecyclerView.Adapter<CheckerTaskListAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onApproveClick(position: Int)
        fun onRejectClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checker_task, parent,
                        false), mListener, checkerInboxFragment)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mListener = onItemClickListener
    }

    fun filterList(filteredList: MutableList<CheckerTask>) {
        items = filteredList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCheckerTaskId.text = items[position].id.toString()
        holder.tvCheckerTaskDate.text = items[position].getDate()
        holder.tvCheckerTaskStatus.text = items[position].status
        holder.tvCheckerTaskMaker.text = items[position].maker
        holder.tvCheckerTaskAction.text = items[position].action
        holder.tvCheckerTaskEntity.text = items[position].entity
        holder.tvCheckerTaskOptionsEntity.text = items[position].entity
        holder.tvCheckerTaskOptionsDate.text = items[position].getDate()

        if (checkerInboxFragment.inBadgeProcessingMode) {
            holder.cbCheckerTask.isChecked = false
            holder.cbCheckerTask.visibility = View.VISIBLE
        } else {
            holder.cbCheckerTask.visibility = View.INVISIBLE
        }
    }

    class ViewHolder(view: View, listener: OnItemClickListener,
                     checkerInboxFragment: CheckerInboxFragment) :
            RecyclerView.ViewHolder(view) {
        val tvCheckerTaskId: TextView = view.tv_checker_task_id
        val tvCheckerTaskDate: TextView = view.tv_checker_task_date
        val tvCheckerTaskStatus: TextView = view.tv_checker_task_status
        val tvCheckerTaskMaker: TextView = view.tv_checker_task_maker
        val tvCheckerTaskAction: TextView = view.tv_checker_task_action
        val tvCheckerTaskEntity: TextView = view.tv_checker_task_entity
        val tvCheckerTaskOptionsEntity: TextView = view.tv_checker_task_options_entity
        val tvCheckerTaskOptionsDate: TextView = view.tv_checker_task_options_date
        val cbCheckerTask: CheckBox = view.cb_checker_task

        private val ivApproveIcon: ImageView = view.iv_approve_icon
        private val ivRejectIcon: ImageView = view.iv_reject_icon
        private val ivDeleteIcon: ImageView = view.iv_delete_icon

        init {
            view.setOnClickListener {
                listener?.let {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION)
                        listener.onItemClick(position)
                    Log.i("ttt", "Item clicked")
                }
                val llCheckerTaskOptions =
                        view.findViewById<LinearLayout>(R.id.ll_checker_task_options)
                if (llCheckerTaskOptions.visibility == View.GONE) {
                    llCheckerTaskOptions.visibility = View.VISIBLE
                } else {
                    llCheckerTaskOptions.visibility = View.GONE
                }

            }

            view.setOnLongClickListener(checkerInboxFragment)

            ivApproveIcon.setOnClickListener {
                listener?.let {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION)
                        listener.onApproveClick(position)
                }
            }

            ivRejectIcon.setOnClickListener {
                listener?.let {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION)
                        listener.onRejectClick(position)
                }
            }

            ivDeleteIcon.setOnClickListener {
                listener?.let {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION)
                        listener.onDeleteClick(position)
                }

            }

            cbCheckerTask.setOnClickListener {
                checkerInboxFragment.prepareSelection(it, adapterPosition)
            }
        }
    }
}