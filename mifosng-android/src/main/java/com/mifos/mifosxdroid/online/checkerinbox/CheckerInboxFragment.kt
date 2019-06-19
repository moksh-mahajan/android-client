package com.mifos.mifosxdroid.online.checkerinbox

import android.app.AlertDialog
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.mifos.mifosxdroid.R
import com.mifos.mifosxdroid.adapters.CheckerTaskListAdapter
import com.mifos.mifosxdroid.core.MifosBaseActivity
import com.mifos.mifosxdroid.core.MifosBaseFragment
import com.mifos.mifosxdroid.core.util.Toaster
import com.mifos.objects.CheckerTask
import kotlinx.android.synthetic.main.checker_inbox_fragment.*
import kotlinx.android.synthetic.main.checker_inbox_tasks_fragment.*
import javax.inject.Inject

class CheckerInboxFragment : MifosBaseFragment() {

    companion object {
        fun newInstance() = CheckerInboxFragment()
    }

    @Inject
    lateinit var factory: CheckerInboxViewModelFactory
    private lateinit var viewModel: CheckerInboxViewModel2
    private lateinit var rvCheckerTasks: RecyclerView
    private lateinit var etSearchTaskByUser : EditText
    private var checkerTaskList = mutableListOf<CheckerTask>()
    private lateinit var checkerTaskListAdapter : CheckerTaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MifosBaseActivity).activityComponent.inject(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.checker_inbox_fragment, container, false)
        setToolbarTitle(resources.getString(R.string.checker_inbox))
        showMifosProgressBar()
        rvCheckerTasks = view.findViewById(R.id.rv_checker_inbox)
        etSearchTaskByUser = view.findViewById(R.id.et_search)
        rvCheckerTasks.layoutManager = LinearLayoutManager(activity)
        rvCheckerTasks.hasFixedSize()
        //rvCheckerTasks.adapter = checkerTaskListAdapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(CheckerInboxViewModel2::class.java)




                viewModel.getCheckerTasks().observe(
                this, Observer { it ->
                    hideMifosProgressBar()
            Log.i("debuggg", "CheckerTasks Live Data changed")
            checkerTaskList.addAll(it!!)
            checkerTaskListAdapter = CheckerTaskListAdapter(checkerTaskList)
            rvCheckerTasks.adapter = checkerTaskListAdapter
//            checkerTaskListAdapter.submitList(it!!)
            Log.i("debuggg", "New List submitted to Adapter")
            checkerTaskListAdapter.setOnItemClickListener(
                    object : CheckerTaskListAdapter.OnItemClickListener {

                        override fun onApproveClick(position: Int) {
                            val builder = AlertDialog.Builder(activity,
                                    R.style.Base_Theme_AppCompat_Light_Dialog_Alert)

                            builder.setTitle("APPROVE")
                                    .setMessage("Do you want to approve this entry?")

                            builder.setPositiveButton("YES") { dialog, which ->
                                //                            viewModel.approveCheckerEntry(checkerTaskListAdapter
//                                    .getCheckerTaskAt(position).id)
                            }

                            builder.setNegativeButton("No") { dialog, which ->

                            }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }

                        override fun onRejectClick(position: Int) {

                            val builder = AlertDialog.Builder(activity,
                                    R.style.Base_Theme_AppCompat_Light_Dialog_Alert)

                            builder.setTitle("REJECT")
                                    .setMessage("Do you want to reject this entry?")

                            builder.setPositiveButton("YES") { dialog, which ->
                                //                            viewModel.rejectCheckerEntry(checkerTaskListAdapter
//                                    .getCheckerTaskAt(position).id)
                            }

                            builder.setNegativeButton("No") { dialog, which ->

                            }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }

                        override fun onDeleteClick(position: Int) {
                            Log.i("abc", position.toString())

                            val builder = AlertDialog.Builder(activity,
                                    R.style.Base_Theme_AppCompat_Light_Dialog_Alert)

                            builder.setTitle("DELETE")
                                    .setMessage("Do you want to delete this entry?")

                            builder.setPositiveButton("YES") { dialog, which ->
                                //viewModel.deleteCheckerEntry(checkerTaskList[position].id)
//                                viewModel.status.observe(this@CheckerInboxFragment,
//                                        Observer { status ->
//                                            status?.let {
//                                                Log.i("xxxab", "Status changed")
//                                                if (!it) {
////                                                    Toast.makeText(activity,
////                                                            "Something went wrong",
////                                                            Toast.LENGTH_LONG).show()
//                                                    Log.i("xxxab", "Status is false")
//                                                    checkerTaskList.removeAt(position)
//                                                    checkerTaskListAdapter.
//                                                            notifyItemRemoved(position)
//                                                } else {
//                                                    Toast.makeText(activity,
//                                                            "Deleted Successfully",
//                                                            Toast.LENGTH_LONG).show()
//
//                                                }
//
//
//
//                                            }
//                                        })

                                viewModel.deleteCheckerEntry(1)

                            }

                            builder.setNegativeButton("No") { dialog, which ->

                            }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }

                        override fun onItemClick(position: Int) {
                            Log.i("abc", position.toString())
                        }

                    }
            )
        })
        }




//        etSearchTaskByUser.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//        })


    }


