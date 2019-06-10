package com.mifos.mifosxdroid.online.checkerinbox

import android.app.PendingIntent.getActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mifos.api.GenericResponse
import com.mifos.api.datamanager.DataManagerCheckerInbox
import com.mifos.mifosxdroid.R
import com.mifos.mifosxdroid.adapters.CheckerTaskListAdapter
import com.mifos.mifosxdroid.base.MvpView
import com.mifos.mifosxdroid.core.MifosBaseActivity
import com.mifos.objects.CheckerTask
import com.mifos.objects.runreports.client.ClientReportTypeItem
import com.mifos.utils.MFErrorParser
import kotlinx.android.synthetic.main.activity_checker_inbox.*
import retrofit2.Call
import retrofit2.adapter.rxjava.HttpException
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.plugins.RxJavaPlugins
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class CheckerInboxActivity : MifosBaseActivity() {


//    @Inject
//    lateinit var datamanager: DataManagerCheckerInbox
    //val datamanager = DataManagerCheckerInbox()
    //var subscription = CompositeSubscription()
     //var factory = CheckerInboxViewModelFactory(DataManagerCheckerInbox(),
        //CompositeSubscription())
    @Inject lateinit var factory : CheckerInboxViewModelFactory

    //val checkerTaks: ArrayList<CheckerTask> = ArrayList()
    private lateinit var checkerInboxViewModel : CheckerInboxViewModel
    private lateinit var adapter: CheckerTaskListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checker_inbox)
        //(getActivity() as MifosBaseActivity).activityComponent.inject(this)
        activityComponent.inject(this)

        //Setting toolbar

        //showProgress("Loading Tasks...")

        btn_checker_tester.setOnClickListener(View.OnClickListener {
            Log.i("def", "Button is working")
            checkerInboxViewModel.deleteCheckerEntry(49056)

        })

        rv_checker_task_list.layoutManager = LinearLayoutManager(this)
        //adapter = CheckerTaskListAdapter()
        checkerInboxViewModel = ViewModelProviders.of(this, factory).
                get(CheckerInboxViewModel::class.java)

        checkerInboxViewModel.getCheckerTasks().observe(this,
                Observer<List<CheckerTask>>() {
                    var checkerTaskListAdapter =
                            CheckerTaskListAdapter(it!!,
                                    this@CheckerInboxActivity)
                    //hideProgress()

                        rv_checker_task_list.adapter = checkerTaskListAdapter
                })


















//        checkerTaks.add(CheckerTask(1, "25112008", "pending.result",
//                "Mifos", "CREATE", "REPORT"))
//        checkerTaks.add(CheckerTask(1, "25112008", "pending.result",
//                "Mifos", "CREATE", "REPORT"))
//        checkerTaks.add(CheckerTask(1, "25112008", "pending.result",
//                "Mifos", "CREATE", "REPORT"))
//        checkerTaks.add(CheckerTask(1, "25112008", "pending.result",
//                "Mifos", "CREATE", "REPORT"))
//        checkerTaks.add(CheckerTask(1, "25112008", "pending.result",
//                "Mifos", "CREATE", "REPORT"))

        //var checkerTaskListAdapter = CheckerTaskListAdapter(checkerTaks, this)
        //rv_checker_task_list.adapter = checkerTaskListAdapter

//        subscription.add(datamanager.getCheckerTaskList()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(object : Subscriber<List<CheckerTask>>() {
//                    override fun onCompleted() {
//
//                    }
//
//                    override fun onError(e: Throwable) {
//
//
//                    }
//
//                    override fun onNext(checkerTasks: List<CheckerTask>) {
//                        tv_test.setText("Hi" + checkerTasks.get(0).id
//                                + checkerTasks.get(0).madeOnDate + checkerTasks.get(0).status
//                                + checkerTasks.get(0).maker + checkerTasks.get(0).action +
//                                checkerTasks.get(0).entity)
//
//                        var checkerTaskListAdapter = CheckerTaskListAdapter(checkerTasks, this@CheckerInboxActivity)
//                        rv_checker_task_list.adapter = checkerTaskListAdapter
//                    }
//                }))




    }

}

