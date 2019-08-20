package com.mifos.mifosxdroid.adapters

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mifos.mifosxdroid.R
import com.mifos.mifosxdroid.online.checkerinbox.CheckerInboxFragment
import com.mifos.objects.CheckerTask
import kotlinx.android.synthetic.main.item_checker_task.view.*

class CheckerTaskListAdapter(private var checkerInboxFragment: CheckerInboxFragment)
    : ListAdapter<CheckerTask, CheckerTaskListAdapter.ViewHolder>(TaskDiffCallback()) {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onApproveClick(position: Int)
        fun onRejectClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<CheckerTask>() {
        override fun areItemsTheSame(oldItem: CheckerTask, newItem: CheckerTask): Boolean {
            return oldItem?.id == newItem?.id
        }

        override fun areContentsTheSame(oldItem: CheckerTask, newItem: CheckerTask): Boolean {
            return oldItem?.resourceId == newItem?.resourceId
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checker_task, parent,
                        false), mListener, checkerInboxFragment)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mListener = onItemClickListener
    }

    override fun submitList(list: MutableList<CheckerTask>?) {
        list?.let {
            val mList = mutableListOf<CheckerTask>()
            mList.addAll(list)
            super.submitList(mList)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCheckerTaskId.text = getItem(position).id.toString()
        holder.tvCheckerTaskDate.text = getItem(position).getDate()
        holder.tvCheckerTaskStatus.text = getItem(position).status
        holder.tvCheckerTaskMaker.text = getItem(position).maker
        holder.tvCheckerTaskAction.text = getItem(position).action
        holder.tvCheckerTaskEntity.text = getItem(position).entity
        holder.tvCheckerTaskOptionsEntity.text = getItem(position).entity
        holder.tvCheckerTaskOptionsDate.text = getItem(position).getDate()

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

