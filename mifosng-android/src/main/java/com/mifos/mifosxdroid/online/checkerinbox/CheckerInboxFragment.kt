package com.mifos.mifosxdroid.online.checkerinbox

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.mifos.mifosxdroid.R
import com.mifos.mifosxdroid.adapters.CheckerTaskListAdapter
import com.mifos.mifosxdroid.core.MaterialDialog
import com.mifos.mifosxdroid.core.MifosBaseActivity
import com.mifos.mifosxdroid.core.MifosBaseFragment
import com.mifos.objects.CheckerTask
import javax.inject.Inject

class CheckerInboxFragment : MifosBaseFragment() {

    companion object {
        fun newInstance() = CheckerInboxFragment()
    }

    @Inject
    lateinit var factory: CheckerInboxViewModelFactory
    private lateinit var viewModel: CheckerInboxViewModel
    private lateinit var rvCheckerTasks: RecyclerView
    private lateinit var checkerTaskListAdapter: CheckerTaskListAdapter
    private lateinit var etSearchTaskByUser: EditText
    private var checkerTaskList = mutableListOf<CheckerTask>()

    private var adapterPosition: Int = -1

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
                    handleStatus(it)
                })

        viewModel.getCheckerTasks().observe(this, Observer {
            hideMifosProgressBar()
            checkerTaskList.addAll(it!!)
            checkerTaskListAdapter = CheckerTaskListAdapter(checkerTaskList)
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
        })
    }

    private fun handleStatus(status: Status?) {
        when (status) {
            Status.APPROVE_SUCCESS -> updateRecyclerViewAfterOperation(adapterPosition,
                    "APPROVED")
            Status.REJECT_SUCCESS -> updateRecyclerViewAfterOperation(adapterPosition,
                    "REJECTED")
            Status.DELETE_SUCCESS -> updateRecyclerViewAfterOperation(adapterPosition,
                    "DELETED")
            Status.APPROVE_ERROR -> showNetworkError()
            Status.REJECT_ERROR -> showNetworkError()
            Status.DELETE_ERROR -> showNetworkError()
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
}