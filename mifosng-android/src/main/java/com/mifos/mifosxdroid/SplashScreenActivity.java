/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package com.mifos.mifosxdroid;

import android.content.Intent;
import android.os.Bundle;

import com.github.mokshmahajan.fpauthdialoglibrary.FpAuthCallback;
import com.github.mokshmahajan.fpauthdialoglibrary.FpAuthDialog;
import com.github.mokshmahajan.fpauthdialoglibrary.FpAuthSupport;
import com.mifos.api.BaseUrl;
import com.mifos.mifosxdroid.core.MifosBaseActivity;
import com.mifos.mifosxdroid.login.LoginActivity;
import com.mifos.mifosxdroid.online.DashboardActivity;
import com.mifos.mifosxdroid.passcode.PassCodeActivity;
import com.mifos.mobile.passcode.utils.PassCodeConstants;
import com.mifos.utils.PrefManager;

import org.jetbrains.annotations.NotNull;

/**
 * This is the First Activity which can be used for initial checks, inits at app Startup
 */
public class SplashScreenActivity extends MifosBaseActivity {
    private FpAuthDialog fpAuthDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!PrefManager.isAuthenticated()) {
            PrefManager.setInstanceUrl(BaseUrl.PROTOCOL_HTTPS
                    + BaseUrl.API_ENDPOINT + BaseUrl.API_PATH);
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        } else {
            //User is authenticated
            if (!FpAuthSupport.checkAvailabiltyAndIfFingerprintRegistered(this)) {
                //Device doesn't support Fingerprint Auth or no Fingerprints are registered
                Intent intent = new Intent(SplashScreenActivity.this,
                        PassCodeActivity.class);
                intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
                startActivity(intent);
                finish();
            } else {
                if (PrefManager.getAuthType().isEmpty()) {
                    startActivity(new Intent(this,
                            FingerprintPasscodePromptActivity.class));
                    finish();
                } else {
                    if (PrefManager.getAuthType()
                            .equalsIgnoreCase("passcode")) {
                        Intent intent = new Intent(SplashScreenActivity.this,
                                PassCodeActivity.class);
                        intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
                        startActivity(intent);
                        finish();
                    } else {
                        //Show Fingerprint Authentication Dialog
                        showFingerprintAuthDialog();
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fpAuthDialog != null)
            fpAuthDialog.dismiss();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (PrefManager.getAuthType()
                .equalsIgnoreCase("fpauth"))
            showFingerprintAuthDialog();
    }

    private void showFingerprintAuthDialog() {
        fpAuthDialog = new FpAuthDialog(this);
        fpAuthDialog.setTitle("Login to Mifos X Android Client")
                .setMessage("Use your Fingerprint to access the app")
                .setCancelText("Login using password instead")
                .setCallback(new FpAuthCallback() {
                    @Override
                    public void onFpAuthSuccess() {
                        startActivity(
                                new Intent(SplashScreenActivity.this,
                                        DashboardActivity.class));
                        SplashScreenActivity.this.finish();
                    }

                    @Override
                    public void onFpAuthFailed(@NotNull String s) {

                    }

                    @Override
                    public void onCancel() {
                        PrefManager.setAuthType("");
                        startActivity(
                                new Intent(SplashScreenActivity.this,
                                        LoginActivity.class));
                        SplashScreenActivity.this.finish();
                    }
                });
        fpAuthDialog.show();
    }
}
