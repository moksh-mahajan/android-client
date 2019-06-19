package com.mifos.mifosxdroid.online.checkerinbox

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel;
import android.util.Log
import com.mifos.api.GenericResponse
import com.mifos.api.datamanager.DataManagerCheckerInbox
import com.mifos.objects.CheckerTask
import com.mifos.objects.checkerinboxandtasks.RescheduleLoansTask
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class CheckerInboxViewModel2 @Inject constructor (
        val dataManager: DataManagerCheckerInbox,
        val subscription: CompositeSubscription
) : ViewModel() {

    private val checkerTasksLive: MutableLiveData<List<CheckerTask>> by lazy {
        Log.i("Debuggg", "Lazy initialization begins...")
        MutableLiveData<List<CheckerTask>>().also {
            Log.i("Debuggg", "Also called...")
            loadCheckerTasks()
        }
    }
//     var status = MutableLiveData<Boolean>()


    fun getCheckerTasks(): LiveData<List<CheckerTask>> {
        Log.i("Debuggg", "getCheckerTasks")
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
                        Log.i("Debuggg", "OnComplete loadCheckerTasks")
                    }

                    override fun onError(e: Throwable) {
                        Log.i("Debuggg", "OnError loadCheckerTasks")

                    }

                    override fun onNext(checkerTasks: List<CheckerTask>) {
                        Log.i("Debuggg", "OnNext1 loadCheckerTasks")
                        checkerTasksLive.postValue(checkerTasks)
                        Log.i("Debuggg", "OnNext2 loadCheckerTasks")
                    }
                }))

    }

    fun approveCheckerEntry(auditId : Int) {
        Log.i("abc", "approveCheckerEntry called")
        subscription.add(dataManager.approveCheckerEntry(auditId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<GenericResponse>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {


                    }

                    override fun onNext(Response : GenericResponse) {
                        Log.i("def", Response.toString())
                    }
                }))
    }

    fun rejectCheckerEntry(auditId : Int) {
        Log.i("abc", "rejectCheckerEntry called")
        subscription.add(dataManager.rejectCheckerEntry(auditId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<GenericResponse>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {


                    }

                    override fun onNext(Response : GenericResponse) {
                        Log.i("reject response", Response.toString())
                    }
                }))
    }

    fun deleteCheckerEntry(auditId : Int) {
        Log.i("debuggg", "deleteCheckerEntry called")
        subscription.add(dataManager.deleteCheckerEntry(auditId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<GenericResponse>() {
                    override fun onCompleted() {
                        Log.i("debuggg", "onCompleted deleteCheckerEntry")
                    }

                    override fun onError(e: Throwable) {
                        Log.i("debuggg", "onError deleteCheckerEntry")
                        //status = MutableLiveData()
                        //status.value = false
                    }

                    override fun onNext(Response : GenericResponse) {
                        Log.i("debuggg", "onNext deleteCheckerEntry")
                        //status.postValue(true)
                    }
                }))
    }
}

