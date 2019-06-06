package com.mifos.api.services
import com.mifos.objects.CheckerTask
import retrofit2.http.GET
import rx.Observable

interface CheckerInboxService {

    @GET("makercheckers")
    fun getCheckerList() : Observable<List<CheckerTask>>
}