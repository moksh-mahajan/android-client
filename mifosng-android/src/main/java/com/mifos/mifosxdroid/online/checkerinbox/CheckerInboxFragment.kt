package com.mifos.mifosxdroid.online.checkerinbox

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mifos.mifosxdroid.R
import com.mifos.mifosxdroid.adapters.CheckerTaskListAdapter
import com.mifos.mifosxdroid.core.MaterialDialog
import com.mifos.mifosxdroid.core.MifosBaseActivity
import com.mifos.mifosxdroid.core.MifosBaseFragment
import com.mifos.objects.CheckerTask
import kotlinx.android.synthetic.main.checker_inbox_fragment.*
import javax.inject.Inject

class CheckerInboxFragment : MifosBaseFragment(), TextWatcher, View.OnLongClickListener {
    override fun onLongClick(p0: View?): Boolean {
        Log.i("abcfg", "Long press")
        viewFlipper.showNext()
        if (inBadgeProcessingMode) {
            selectedCheckerTaskList.clear()
            inBadgeProcessingMode = false
            checkerTaskListAdapter.notifyDataSetChanged()
        } else {
            inBadgeProcessingMode = true
            checkerTaskListAdapter.notifyDataSetChanged()
        }
        return true
    }

    companion object {
        fun newInstance() = CheckerInboxFragment()
    }

    @Inject
    lateinit var factory: CheckerInboxViewModelFactory
    private lateinit var viewModel: CheckerInboxViewModel
    private lateinit var rvCheckerTasks: RecyclerView
    private lateinit var checkerTaskListAdapter: CheckerTaskListAdapter
    private lateinit var etSearchTaskByUser: EditText
    private lateinit var ivSearchFilter: ImageView
    private lateinit var ivBatchApprove: ImageView
    private lateinit var ivBatchReject: ImageView
    private lateinit var ivBatchDelete: ImageView
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var tvNoOfCheckerTasks: TextView
    private var checkerTaskList = mutableListOf<CheckerTask>()
    private var selectedCheckerTaskList = mutableListOf<CheckerTask>()

    private var adapterPosition: Int = -1
    //    private var count: Int = 0
    var inBadgeProcessingMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MifosBaseActivity).activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.checker_inbox_fragment,
                container, false)
        setToolbarTitle(resources.getString(R.string.checker_inbox))
        showMifosProgressBar()
        rvCheckerTasks = view.findViewById(R.id.rv_checker_inbox)
        etSearchTaskByUser = view.findViewById(R.id.et_search)
        ivSearchFilter = view.findViewById(R.id.iv_filter_search_icon)
        viewFlipper = view.findViewById(R.id.view_flipper)
        tvNoOfCheckerTasks = view.findViewById(R.id.tv_no_of_selected_tasks)
        ivBatchApprove = view.findViewById(R.id.iv_batch_approve_icon)
        ivBatchReject = view.findViewById(R.id.iv_batch_reject_icon)
        ivBatchDelete = view.findViewById(R.id.iv_batch_delete_icon)
        etSearchTaskByUser.addTextChangedListener(this)
        rvCheckerTasks.layoutManager = LinearLayoutManager(activity)
        rvCheckerTasks.hasFixedSize()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory)
                .get(CheckerInboxViewModel::class.java)

        viewModel.getStatus().observe(this@CheckerInboxFragment,
                Observer {
                    Log.i("abcde", "Status is" + it)
                    handleStatus(it)
                })

//        viewModel.getCompleteStatus().observe(this@CheckerInboxFragment,
//                Observer {
//                    handleCompleteStatus(it)
//                })

        viewModel.getCheckerTasks().observe(this, Observer {
            hideMifosProgressBar()
//            checkerTaskList.clear()
//            adapterPosition = -1
            Log.i("Live", "Live Data changed")
            checkerTaskList.addAll(it!!)
            checkerTaskListAdapter = CheckerTaskListAdapter(checkerTaskList,
                    this@CheckerInboxFragment)
            rvCheckerTasks.adapter = checkerTaskListAdapter

            checkerTaskListAdapter.setOnItemClickListener(
                    object : CheckerTaskListAdapter.OnItemClickListener {

                        override fun onApproveClick(position: Int) {
                            adapterPosition = position
                            MaterialDialog.Builder().init(activity)
                                    .setTitle("APPROVE")
                                    .setMessage("Do you want to approve this entry?")
                                    .setPositiveButton("YES") { dialog, which ->
                                        viewModel.approveCheckerEntry(checkerTaskList[position].id)
                                    }
                                    .setNegativeButton("CANCEL")
                                    .show()
                        }

                        override fun onRejectClick(position: Int) {
                            adapterPosition = position
                            MaterialDialog.Builder().init(activity)
                                    .setTitle("REJECT")
                                    .setMessage("Do you want to reject this entry?")
                                    .setPositiveButton("YES") { dialog, which ->
                                        viewModel.rejectCheckerEntry(checkerTaskList[position].id)
                                    }
                                    .setNegativeButton("CANCEL")
                                    .show()
                        }

                        override fun onDeleteClick(position: Int) {
                            adapterPosition = position
                            MaterialDialog.Builder().init(activity)
                                    .setTitle("DELETE")
                                    .setMessage("Do you want to delete this entry?")
                                    .setPositiveButton("YES") { dialog, which ->
                                        viewModel.deleteCheckerEntry(checkerTaskList[position].id)
                                    }
                                    .setNegativeButton("CANCEL")
                                    .show()
                        }

                        override fun onItemClick(position: Int) {
                        }
                    })

            ivSearchFilter.setOnClickListener {
                Log.i("abcde", "Filter Clicked")
                //for (checkerTask in selectedCheckerTaskList) {
                //Log.i("abcde", checkerTask.toString())
                viewModel.deleteCheckerEntry(selectedCheckerTaskList[0].id)
            }

            ivBatchApprove.setOnClickListener {
                if (selectedCheckerTaskList.isNotEmpty()) {
                    MaterialDialog.Builder().init(activity)
                            .setTitle("APPROVE")
                            .setMessage("Approve selected entries?")
                            .setPositiveButton("YES") { dialog, which ->
                                viewModel.approveCheckerEntry(selectedCheckerTaskList[0].id)
                            }
                            .setNegativeButton("CANCEL")
                            .show()
                } else {
                    Toast.makeText(activity, "No task selected", Toast.LENGTH_SHORT).show()
                }
            }

            ivBatchReject.setOnClickListener {
                if (selectedCheckerTaskList.isNotEmpty()) {
                    MaterialDialog.Builder().init(activity)
                            .setTitle("REJECT")
                            .setMessage("Reject selected entries?")
                            .setPositiveButton("YES") { dialog, which ->
                                viewModel.rejectCheckerEntry(selectedCheckerTaskList[0].id)
                            }
                            .setNegativeButton("CANCEL")
                            .show()
                } else {
                    Toast.makeText(activity, "No task selected", Toast.LENGTH_SHORT).show()
                }
            }

            ivBatchDelete.setOnClickListener {
                if (selectedCheckerTaskList.isNotEmpty()) {
                    MaterialDialog.Builder().init(activity)
                            .setTitle("DELETE")
                            .setMessage("Delete selected entries?")
                            .setPositiveButton("YES") { dialog, which ->
                                viewModel.deleteCheckerEntry(selectedCheckerTaskList[0].id)
                            }
                            .setNegativeButton("CANCEL")
                            .show()
                } else {
                    Toast.makeText(activity, "No task selected", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun handleStatus(status: Status?) {
        when (status) {
            Status.APPROVE_SUCCESS -> {
                if (selectedCheckerTaskList.isNotEmpty()) {
                    val task = selectedCheckerTaskList.removeAt(0)
                    checkerTaskList.remove(task)
                    if (selectedCheckerTaskList.isNotEmpty()) {
                        viewModel.approveCheckerEntry(
                                selectedCheckerTaskList[0].id)
                    } else {
                        tvNoOfCheckerTasks.text = "No"
                        inBadgeProcessingMode = false
                        checkerTaskListAdapter.notifyDataSetChanged()
                        viewFlipper.showNext()
                    }
                } else {
                    updateRecyclerViewAfterOperation(adapterPosition,
                            "APPROVED")
                }
            }

            Status.REJECT_SUCCESS -> {
                if (selectedCheckerTaskList.isNotEmpty()) {
                    val task = selectedCheckerTaskList.removeAt(0)
                    checkerTaskList.remove(task)
                    if (selectedCheckerTaskList.isNotEmpty()) {
                        viewModel.rejectCheckerEntry(
                                selectedCheckerTaskList[0].id)
                    } else {
                        tvNoOfCheckerTasks.text = "No"
                        inBadgeProcessingMode = false
                        checkerTaskListAdapter.notifyDataSetChanged()
                        viewFlipper.showNext()
                    }
                } else {
                    updateRecyclerViewAfterOperation(adapterPosition,
                            "REJECTED")
                }
            }
            Status.DELETE_SUCCESS -> {
                if (selectedCheckerTaskList.isNotEmpty()) {
                    Log.i("abcde", "SelectedList is not empty. No. of elements = "
                            + selectedCheckerTaskList.size)
                    val task = selectedCheckerTaskList.removeAt(0)
                    Log.i("abcde", "Task removed from selectedList: " + task.id)
                    checkerTaskList.remove(task)
                    Log.i("abcde", "Task removed from checkerTaskList: " + task.id)
                    if (selectedCheckerTaskList.isNotEmpty()) {
                        Log.i("abcde", "SelectedList is not empty. No. of elements = "
                                + selectedCheckerTaskList.size)

                        viewModel.deleteCheckerEntry(
                                selectedCheckerTaskList[0].id)
                        Log.i("abcde", "Delete call initiated for other element")
                    } else {
                        Log.i("abcde", "SelectedList is empty: "
                                + selectedCheckerTaskList.size)
                        tvNoOfCheckerTasks.text = "No"
                        inBadgeProcessingMode = false
                        checkerTaskListAdapter.notifyDataSetChanged()
                        viewFlipper.showNext()
                    }
                } else {
                    updateRecyclerViewAfterOperation(adapterPosition,
                            "DELETED")
                }
            }
            Status.APPROVE_ERROR -> {
                if (selectedCheckerTaskList.isNotEmpty()) {
                    selectedCheckerTaskList.removeAt(0)
                    if (selectedCheckerTaskList.isNotEmpty()) {
                        viewModel.approveCheckerEntry(
                                selectedCheckerTaskList[0].id)
                    } else {
                        checkerTaskListAdapter.notifyDataSetChanged()
                    }
                } else {
                    showNetworkError()
                }
            }
            Status.REJECT_ERROR -> {
                if (selectedCheckerTaskList.isNotEmpty()) {
                    selectedCheckerTaskList.removeAt(0)
                    if (selectedCheckerTaskList.isNotEmpty()) {
                        viewModel.rejectCheckerEntry(
                                selectedCheckerTaskList[0].id)
                    } else {
                        tvNoOfCheckerTasks.text = "No"
                        inBadgeProcessingMode = false
                        checkerTaskListAdapter.notifyDataSetChanged()
                        viewFlipper.showNext()
                    }
                } else {
                    showNetworkError()
                }
            }
            Status.DELETE_ERROR -> {
                if (selectedCheckerTaskList.isNotEmpty()) {
                    selectedCheckerTaskList.removeAt(0)
                    if (selectedCheckerTaskList.isNotEmpty()) {
                        viewModel.deleteCheckerEntry(
                                selectedCheckerTaskList[0].id)
                    } else {
                        tvNoOfCheckerTasks.text = "No"
                        inBadgeProcessingMode = false
                        checkerTaskListAdapter.notifyDataSetChanged()
                        viewFlipper.showNext()
                    }
                } else {
                    showNetworkError()
                }
            }
        }
    }

    private fun updateRecyclerViewAfterOperation(position: Int, action: String) {
        checkerTaskList.removeAt(position)
        checkerTaskListAdapter.notifyItemRemoved(position)
        Toast.makeText(activity, "ENTRY $action", Toast.LENGTH_SHORT).show()
    }

    private fun showNetworkError() {
        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
    }

    override fun afterTextChanged(p0: Editable?) {
        filter(p0.toString())
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    private fun filter(text: String) {
        val filteredList = mutableListOf<CheckerTask>()

        for (checkerTask in checkerTaskList) {
            if (checkerTask.maker.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(checkerTask)
            }
        }
        checkerTaskListAdapter.filterList(filteredList)
    }

    fun prepareSelection(view: View, position: Int) {
        if ((view as CheckBox).isChecked) {
            selectedCheckerTaskList.add(checkerTaskList[position])
            tvNoOfCheckerTasks.text = selectedCheckerTaskList.size.toString()
            Log.i("abcdef", "List Count: " + selectedCheckerTaskList.size)
        } else {
            selectedCheckerTaskList.remove(checkerTaskList[position])
            tvNoOfCheckerTasks.text = selectedCheckerTaskList.size.toString()
            Log.i("abcdef", "List Count: " + selectedCheckerTaskList.size)
        }
    }
}