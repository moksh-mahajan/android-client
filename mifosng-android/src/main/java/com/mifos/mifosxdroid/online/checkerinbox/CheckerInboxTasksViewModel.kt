package com.mifos.mifosxdroid.online.checkerinbox

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel;
import android.util.Log
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
        val subscription: CompositeSubscription
) : ViewModel() {

    private val checkerTasksLive: MutableLiveData<List<CheckerTask>> by lazy {
        Log.i("abc", "Lazy initialization begins...")
        MutableLiveData<List<CheckerTask>>().also {
            Log.i("abc", "Also called...")
            loadCheckerTasks()
        }
    }

    var status = MutableLiveData<Boolean>()

    private val rescheduleLoanTasksLive: MutableLiveData<List<RescheduleLoansTask>> by lazy {
        Log.i("abc", "Lazy initialization begins...")
        MutableLiveData<List<RescheduleLoansTask>>().also {
            Log.i("abc", "Also called...")
            loadRescheduleLoanTasks()
        }
    }

    fun getRescheduleLoanTasks(): MutableLiveData<List<RescheduleLoansTask>> {
        Log.i("abc", "getRescheduleLoanTasks")
        return rescheduleLoanTasksLive
    }

    private fun loadRescheduleLoanTasks() {
        Log.i("abc", "loadusers called")
        // Do an asynchronous operation to fetch users.
        subscription.add(dataManager.getRechdeduleLoansTaskList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<List<RescheduleLoansTask>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {


                    }

                    override fun onNext(rescheduleLoanTasks: List<RescheduleLoansTask>) {
                        //Log.i("abc", "onNext called")
                        rescheduleLoanTasksLive.postValue(rescheduleLoanTasks)
                        //Log.i("abc", "After postValue "+ checkerTasks)
                    }
                }))
    }


    fun getCheckerTasks(): LiveData<List<CheckerTask>> {
        Log.i("abc", "getCheckerTasks")
        return checkerTasksLive
    }

    private fun loadCheckerTasks() {
        Log.i("abc", "loadusers called")
        // Do an asynchronous operation to fetch users.
        subscription.add(dataManager.getCheckerTaskList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<List<CheckerTask>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {


                    }

                    override fun onNext(checkerTasks: List<CheckerTask>) {
                        //Log.i("abc", "onNext called")
                        checkerTasksLive.postValue(checkerTasks)
                        status.value = true
                        //Log.i("abc", "After postValue "+ checkerTasks)
                    }
                }))

    }
}
