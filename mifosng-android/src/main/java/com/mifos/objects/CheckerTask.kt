package com.mifos.objects

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class CheckerTask(@SerializedName("id") var id: Int,
                       @SerializedName("madeOnDate") var madeOnDate: String,
                       @SerializedName("processingResult") var status: String,
                       @SerializedName("maker") var maker: String,
                       @SerializedName("actionName") var action: String,
                       @SerializedName("entityName") var entity: String) {

    fun getDate(): String {
        val date = Date(madeOnDate.toLong())
        val dateFormat = SimpleDateFormat("dd MMM yyyy")
        return dateFormat.format(date)
    }
}