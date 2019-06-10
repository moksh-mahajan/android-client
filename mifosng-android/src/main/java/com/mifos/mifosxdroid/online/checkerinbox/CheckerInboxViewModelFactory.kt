package com.mifos.mifosxdroid.online.checkerinbox

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mifos.api.datamanager.DataManagerCheckerInbox
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

//@Singleton
class CheckerInboxViewModelFactory @Inject constructor(
        val dataManagerCheckerInbox: DataManagerCheckerInbox) : ViewModelProvider.NewInstanceFactory() {
//    lateinit var dataManager : DataManagerCheckerInbox
    val subscription = CompositeSubscription()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckerInboxViewModel::class.java)) {
            return CheckerInboxViewModel(dataManagerCheckerInbox, subscription) as T
        }
        if (modelClass.isAssignableFrom(CheckerInboxTasksViewModel::class.java)) {
            return CheckerInboxTasksViewModel(dataManagerCheckerInbox, subscription) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

}