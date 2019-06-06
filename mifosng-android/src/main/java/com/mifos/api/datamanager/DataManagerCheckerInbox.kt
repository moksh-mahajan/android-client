package com.mifos.api.datamanager

import com.mifos.api.BaseApiManager
import com.mifos.objects.CheckerTask
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class DataManagerCheckerInbox @Inject constructor(baseApiManager: BaseApiManager){
class DataManagerCheckerInbox @Inject constructor() {
    val mBaseApiManager = BaseApiManager()

    fun getCheckerTaskList () : Observable<List<CheckerTask>> {
        return mBaseApiManager.checkerInboxApi.getCheckerList()
    }
}

