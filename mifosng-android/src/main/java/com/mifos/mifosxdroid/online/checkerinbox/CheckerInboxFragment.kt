package com.mifos.mifosxdroid.online.checkerinbox

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mifos.mifosxdroid.R

class CheckerInboxFragment : Fragment() {

    companion object {
        fun newInstance() = CheckerInboxFragment()
    }

    private lateinit var viewModel: CheckerInboxViewModel2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.checker_inbox_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CheckerInboxViewModel2::class.java)
        // TODO: Use the ViewModel
    }

}
