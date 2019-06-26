package com.mifos.mifosxdroid.online.checkerinbox

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel;
import com.mifos.api.datamanager.DataManagerCheckerInbox
import com.mifos.objects.CheckerTask
import com.mifos.objects.checkerinboxandtasks.RescheduleLoansTask
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class CheckerInboxTasksViewModel @Inject constructor(
        val dataManager: DataManagerCheckerInbox,
        val subscription: CompositeSubscription): ViewModel() {

    private val checkerTasksLive: MutableLiveData<List<CheckerTask>> by lazy {
        MutableLiveData<List<CheckerTask>>().also {
            loadCheckerTasks()
        }
    }

    var status = MutableLiveData<Boolean>()

    private val rescheduleLoanTasksLive: MutableLiveData<List<RescheduleLoansTask>> by lazy {
        MutableLiveData<List<RescheduleLoansTask>>().also {
            loadRescheduleLoanTasks()
        }
    }

    fun getRescheduleLoanTasks(): MutableLiveData<List<RescheduleLoansTask>> {
        return rescheduleLoanTasksLive
    }

    private fun loadRescheduleLoanTasks() {
        subscription.add(dataManager.getRechdeduleLoansTaskList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<List<RescheduleLoansTask>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {


                    }

                    override fun onNext(rescheduleLoanTasks: List<RescheduleLoansTask>) {
                        rescheduleLoanTasksLive.postValue(rescheduleLoanTasks)
                    }
                }))
    }


    fun getCheckerTasks(): LiveData<List<CheckerTask>> {
        return checkerTasksLive
    }

    private fun loadCheckerTasks() {
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
                        status.value = true
                    }
                }))
    }
}