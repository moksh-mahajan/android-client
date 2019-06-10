package com.mifos.mifosxdroid.online.checkerinbox

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.mifos.mifosxdroid.R
import com.mifos.mifosxdroid.adapters.CheckerTaskListAdapter
import com.mifos.mifosxdroid.core.MifosBaseActivity
import com.mifos.mifosxdroid.core.MifosBaseFragment
import com.mifos.objects.CheckerTask
import com.mifos.objects.checkerinboxandtasks.RescheduleLoansTask
import kotlinx.android.synthetic.main.activity_checker_inbox.*
import kotlinx.android.synthetic.main.checker_inbox_tasks_fragment.*
import javax.inject.Inject

class CheckerInboxTasksFragment : MifosBaseFragment() {

    companion object {
        fun newInstance() = CheckerInboxTasksFragment()
    }
    @Inject lateinit var factory : CheckerInboxViewModelFactory
    private lateinit var viewModel: CheckerInboxTasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MifosBaseActivity).activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.checker_inbox_tasks_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(
                this, factory
        ).get(CheckerInboxTasksViewModel::class.java)

        viewModel.getCheckerTasks().observe(this,
                Observer<List<CheckerTask>> {
                    badge_checker_inbox.text = it?.size.toString()
                })

        viewModel.getRescheduleLoanTasks().observe(this,
                Observer<List<RescheduleLoansTask>> {
                    badge_reschedule_loan.text = it?.size.toString()
                })

        rl_checker_inbox.setOnClickListener {
            Log.i("abc", "Checker Inbox clicked")
            Toast.makeText(activity,
                    "Hi CheckerInbox", Toast.LENGTH_LONG).show()

            val fragmentTransaction = activity!!
                    .supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack("ClientCategory")
            fragmentTransaction.replace(R.id.container,
                    CheckerInboxFragment.newInstance()).commit()


        }


    }

}
