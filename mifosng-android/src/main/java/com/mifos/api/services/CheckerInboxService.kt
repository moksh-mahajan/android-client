package com.mifos.api.services
import com.mifos.api.GenericResponse
import com.mifos.api.model.APIEndPoint
import com.mifos.objects.CheckerTask
import com.mifos.objects.checkerinboxandtasks.RescheduleLoansTask
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

interface CheckerInboxService {

    @GET("makercheckers")
    fun getCheckerList() : Observable<List<CheckerTask>>

    @POST(APIEndPoint.MAKERCHECKER + "/{auditId}?command=approve")
    fun approveCheckerEntry(@Path("auditId") auditId : Int) : Observable<GenericResponse>

    @POST(APIEndPoint.MAKERCHECKER + "/{auditId}?command=reject")
    fun rejectCheckerEntry(@Path("auditId") auditId : Int) : Observable<GenericResponse>

    @DELETE(APIEndPoint.MAKERCHECKER + "/{auditId}")
    fun deleteCheckerEntry(@Path("auditId") auditId : Int) : Observable<GenericResponse>

    @GET("rescheduleloans?command=pending")
    fun getRescheduleLoansTaskList() : Observable<List<RescheduleLoansTask>>


}