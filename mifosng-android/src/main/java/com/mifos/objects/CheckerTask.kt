package com.mifos.objects

import com.google.gson.annotations.SerializedName

data class CheckerTask (@SerializedName("id") var id: Int,
                        @SerializedName("madeOnDate") var madeOnDate: String,
                        @SerializedName("processingResult") var status: String,
                        @SerializedName("maker") var maker: String,
                        @SerializedName("actionName") var action: String,
                        @SerializedName("entityName") var entity: String) {

}