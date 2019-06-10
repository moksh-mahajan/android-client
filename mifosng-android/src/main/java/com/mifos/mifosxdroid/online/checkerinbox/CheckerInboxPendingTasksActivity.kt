package com.mifos.mifosxdroid.online.checkerinbox

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mifos.mifosxdroid.R
import com.mifos.mifosxdroid.core.MifosBaseActivity
import com.mifos.mifosxdroid.online.runreports.reportcategory.ReportCategoryFragment
import kotlinx.android.synthetic.main.toolbar.*

class CheckerInboxPendingTasksActivity : MifosBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)

        showBackButton()
        setToolbarTitle(resources.getString(R.string.checker_inbox_and_pending_tasks))

        val fragment = CheckerInboxTasksFragment()
        replaceFragment(fragment, false, R.id.container)
    }
}
