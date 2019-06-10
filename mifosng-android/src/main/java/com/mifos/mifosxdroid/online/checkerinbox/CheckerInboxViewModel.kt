package com.mifos.mifosxdroid.online.checkerinbox

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.mifos.api.GenericResponse
import com.mifos.api.datamanager.DataManagerCheckerInbox
import com.mifos.objects.CheckerTask
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class CheckerInboxViewModel @Inject constructor (
        val dataManager: DataManagerCheckerInbox,
        val subscription: CompositeSubscription
) : ViewModel() {
    //val subscription = CompositeSubscription()
    //val dataManager = DataManagerCheckerInbox()
    private val checkerTasksLive: MutableLiveData<List<CheckerTask>> by lazy {
        Log.i("abc", "Lazy initialization begins...")
        MutableLiveData<List<CheckerTask>>().also {
            Log.i("abc", "Also called...")
            loadCheckerTasks()
        }
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
                        Log.i("abc", "onNext called")
                        checkerTasksLive.postValue(checkerTasks)
                        Log.i("abc", "After postValue "+ checkerTasks)
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
        Log.i("abc", "approveCheckerEntry called")
        subscription.add(dataManager.deleteCheckerEntry(auditId)
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

    override fun onCleared() {
        super.onCleared()
        subscription.unsubscribe()
        Log.i("abc", "ViewModel Cleared"+subscription.isUnsubscribed)

    }
}