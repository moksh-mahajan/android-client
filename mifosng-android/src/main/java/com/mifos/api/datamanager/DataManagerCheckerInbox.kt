package com.mifos.api.datamanager

import com.mifos.api.BaseApiManager
import com.mifos.api.GenericResponse
import com.mifos.objects.CheckerTask
import com.mifos.objects.checkerinboxandtasks.RescheduleLoansTask
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton


class DataManagerCheckerInbox @Inject constructor(){
//class DataManagerCheckerInbox  {
    val mBaseApiManager = BaseApiManager()

    fun getCheckerTaskList() : Observable<List<CheckerTask>> {
        return mBaseApiManager.checkerInboxApi.getCheckerList()
    }

    fun approveCheckerEntry(auditId : Int) : Observable<GenericResponse> {
        return mBaseApiManager.checkerInboxApi.approveCheckerEntry(auditId)
    }

    fun rejectCheckerEntry(auditId: Int) : Observable<GenericResponse> {
        return mBaseApiManager.checkerInboxApi.rejectCheckerEntry(auditId)
    }

    fun deleteCheckerEntry(auditId: Int) : Observable<GenericResponse> {
        return mBaseApiManager.checkerInboxApi.deleteCheckerEntry(auditId)
    }

    fun getRechdeduleLoansTaskList() : Observable<List<RescheduleLoansTask>>{
        return mBaseApiManager.checkerInboxApi.getRescheduleLoansTaskList()
    }


}

