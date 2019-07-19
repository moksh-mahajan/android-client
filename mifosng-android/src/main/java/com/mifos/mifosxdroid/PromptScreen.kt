package com.mifos.mifosxdroid

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mifos.mifosxdroid.login.LoginActivity
import com.mifos.mifosxdroid.online.DashboardActivity
import com.mifos.mifosxdroid.passcode.PassCodeActivity
import com.mifos.mobile.passcode.utils.PassCodeConstants
import com.mifos.utils.PrefManager
import com.moksh.fingerprintauthenticationlibrary.FPAuthCallback
import com.moksh.fingerprintauthenticationlibrary.FPAuthDialog
import kotlinx.android.synthetic.main.activity_prompt_screen.*

class PromptScreen : AppCompatActivity() {
private lateinit var fpDialog: FPAuthDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prompt_screen)

        btn_passcode.setOnClickListener {
            PrefManager.setAuthenticationType("Passcode")
            val intent = Intent(this,
                    PassCodeActivity::class.java)
            intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true)
            startActivity(intent)
            finish()
        }

        btn_fp_auth.setOnClickListener {
            PrefManager.setAuthenticationType("FPAuth")
            Log.i("TAGGER", "Auth Type set to: " +
                    PrefManager.getAuthenticationType())

            fpDialog = FPAuthDialog.create(this)
                    .setTitle("Login to Mifos X")
                    .setMessage("Use Fingerprint to access app")
                    .setCallback(object : FPAuthCallback {
                        override fun onBelowAndroidMarshmallow() {

                        }

                        override fun onNoFingerprintScannerAvailable() {

                        }

                        override fun onNoFingerprintRegistered() {

                        }

                        override fun onFingerprintAuthSuccess(s: String) {
                            startActivity(Intent(this@PromptScreen,
                                    DashboardActivity::class.java))
                            this@PromptScreen.finish()
                        }

                        override fun onFingerprintAuthFailed(s: String) {

                        }

                        override fun onCancel() {
                            PrefManager.setAuthenticationType("")
                            startActivity(Intent(this@PromptScreen,
                                    LoginActivity::class.java))
                            this@PromptScreen.finish()
                        }
                    })
            fpDialog.show()

        }
    }

    override fun onRestart() {
        super.onRestart()
        if (PrefManager.getAuthenticationType().equals("fpauth",true)) {
            fpDialog.dismiss()

            FPAuthDialog.create(this)
                    .setTitle("Login to Mifos X")
                    .setMessage("Use Fingerprint to access app")
                    .setCallback(object : FPAuthCallback {
                        override fun onBelowAndroidMarshmallow() {

                        }

                        override fun onNoFingerprintScannerAvailable() {

                        }

                        override fun onNoFingerprintRegistered() {

                        }

                        override fun onFingerprintAuthSuccess(s: String) {
                            startActivity(Intent(this@PromptScreen,
                                    DashboardActivity::class.java))
                            this@PromptScreen.finish()
                        }

                        override fun onFingerprintAuthFailed(s: String) {

                        }

                        override fun onCancel() {
                            PrefManager.setAuthenticationType("")
                            startActivity(Intent(this@PromptScreen,
                                    LoginActivity::class.java))
                            this@PromptScreen.finish()
                        }
                    }).show()
        }
    }
}
