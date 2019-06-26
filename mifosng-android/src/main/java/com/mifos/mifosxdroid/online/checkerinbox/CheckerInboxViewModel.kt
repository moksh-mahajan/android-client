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

enum class Status {
    APPROVE_SUCCESS,
    APPROVE_ERROR,
    APPROVE_COMPLETED,
    REJECT_SUCCESS,
    REJECT_ERROR,
    REJECT_COMPLETED,
    DELETE_SUCCESS,
    DELETE_ERROR,
    DELETE_COMPLETED
}

//enum class CompleteStatus {
//    DELETE_COMPLETED,
//    APPROVE_COMPLETED,
//    REJECT_COMPLETED
//}

class CheckerInboxViewModel @Inject constructor(
        val dataManager: DataManagerCheckerInbox,
        val subscription: CompositeSubscription
) : ViewModel() {

    private val checkerTasksLive: MutableLiveData<List<CheckerTask>> by lazy {
        MutableLiveData<List<CheckerTask>>().also {
            loadCheckerTasks()
        }
    }

    private val status = MutableLiveData<Status>()

    fun getCheckerTasks(): LiveData<List<CheckerTask>> {
        return checkerTasksLive
    }

//    private val completeStatus = MutableLiveData<CompleteStatus>()

    fun getStatus(): LiveData<Status> {
        return status
    }

//    fun getCompleteStatus(): LiveData<CompleteStatus> {
//        return completeStatus
//    }

     fun loadCheckerTasks() {
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

    fun approveCheckerEntry(auditId: Int) {
        subscription.add(dataManager.approveCheckerEntry(auditId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<GenericResponse>() {
                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        status.value = Status.APPROVE_ERROR
                    }

                    override fun onNext(Response: GenericResponse) {
                        status.value = Status.APPROVE_SUCCESS
                    }
                }))
    }

    fun rejectCheckerEntry(auditId: Int) {
        subscription.add(dataManager.rejectCheckerEntry(auditId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<GenericResponse>() {
                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        status.value = Status.REJECT_ERROR
                    }

                    override fun onNext(Response: GenericResponse) {
                        status.value = Status.REJECT_SUCCESS
                    }
                }))
    }

    fun deleteCheckerEntry(auditId: Int) {
        Log.i("abcde", "Id supplied to deleteCheckerEntry: " + auditId)
        subscription.add(dataManager.deleteCheckerEntry(auditId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<GenericResponse>() {
                    override fun onCompleted() {
//                        completeStatus.value = CompleteStatus.DELETE_COMPLETED
                    }

                    override fun onError(e: Throwable) {
                        Log.i("abcde", "onError called")
                        status.value = Status.DELETE_ERROR
                    }

                    override fun onNext(Response: GenericResponse) {
                        Log.i("abcde", "onNext called")
                        status.value = Status.DELETE_SUCCESS
                    }
                }))
    }
}