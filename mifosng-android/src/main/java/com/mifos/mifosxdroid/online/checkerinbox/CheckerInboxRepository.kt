package com.mifos.mifosxdroid.online.checkerinbox

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mifos.api.datamanager.DataManagerCheckerInbox
import com.mifos.mifosxdroid.adapters.CheckerTaskListAdapter
import com.mifos.objects.CheckerTask
import kotlinx.android.synthetic.main.activity_checker_inbox.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class CheckerInboxRepository {
    val checkerTasksLive = MutableLiveData<List<CheckerTask>>()
    val subscription = CompositeSubscription()
    val dataManager = DataManagerCheckerInbox()

    fun fetchData() {
        subscription.add(dataManager.getCheckerTaskList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<List<CheckerTask>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {


                    }

                    override fun onNext(checkerTasks: List<CheckerTask>) {
                        checkerTasksLive.postValue(checkerTasks)
                    }
                }))
    }

    fun getCheckerTasks() : LiveData<List<CheckerTask>>{
        return checkerTasksLive
    }

    fun postServiceRequest()
    {
        fetchData()
    }
}