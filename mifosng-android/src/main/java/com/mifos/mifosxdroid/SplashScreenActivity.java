/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package com.mifos.mifosxdroid;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mifos.api.BaseUrl;
import com.mifos.mifosxdroid.core.MifosBaseActivity;
import com.mifos.mifosxdroid.login.LoginActivity;
import com.mifos.mifosxdroid.online.DashboardActivity;
import com.mifos.mifosxdroid.passcode.PassCodeActivity;
import com.mifos.mobile.passcode.utils.PassCodeConstants;
import com.mifos.utils.PrefManager;
import com.moksh.fingerprintauthenticationlibrary.FPAuthCallback;
import com.moksh.fingerprintauthenticationlibrary.FPAuthDialog;

import org.jetbrains.annotations.NotNull;


/**
 * This is the First Activity which can be used for initial checks, inits at app Startup
 */
public class SplashScreenActivity extends AppCompatActivity {
FPAuthDialog fpdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!PrefManager.isAuthenticated()) {
            PrefManager.setInstanceUrl(BaseUrl.PROTOCOL_HTTPS
                    + BaseUrl.API_ENDPOINT + BaseUrl.API_PATH);
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        } else {
//            Intent intent = new Intent(SplashScreenActivity.this,
//                    PassCodeActivity.class);
//            intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
//            startActivity(intent);

            FingerprintManagerCompat fp = FingerprintManagerCompat.from(this);
            if (!fp.hasEnrolledFingerprints() || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Intent intent = new Intent(SplashScreenActivity.this,
                        PassCodeActivity.class);
                intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
                startActivity(intent);
                finish();
            } else {
                // Show a prompt to user to choose between Passcode and FingerPrint
                // Save this value in Shared Preferences
                // Show this prompt only the first time
                if (PrefManager.getAuthenticationType().isEmpty()) {
                    startActivity(new Intent(this, PromptScreen.class));
                    finish();
                } else {
                    if(PrefManager.getAuthenticationType()
                            .equalsIgnoreCase("passcode")) {
                        //Toast.makeText(SplashScreenActivity.this,
                                //"passcode", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashScreenActivity.this,
                                PassCodeActivity.class);
                        intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
                        startActivity(intent);

                    } else {
                        //Show Finger print dialog
                        //Toast.makeText(SplashScreenActivity.this,
                                //"fingerprint", Toast.LENGTH_SHORT).show();
                        fpdialog = FPAuthDialog.create(this)
                                .setTitle("Login to Mifos X")
                                .setMessage("Use Fingerprint to access app")
                                .setCallback(new FPAuthCallback() {
                                    @Override
                                    public void onBelowAndroidMarshmallow() {
                                        Intent intent = new Intent(SplashScreenActivity.this,
                                                PassCodeActivity.class);
                                        intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onNoFingerprintScannerAvailable() {
                                        Intent intent = new Intent(SplashScreenActivity.this,
                                                PassCodeActivity.class);
                                        intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onNoFingerprintRegistered() {
                                        Intent intent = new Intent(SplashScreenActivity.this,
                                                PassCodeActivity.class);
                                        intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFingerprintAuthSuccess(@NotNull String s) {
                                        startActivity(new Intent(SplashScreenActivity.this,
                                                DashboardActivity.class));
                                        SplashScreenActivity.this.finish();
                                    }

                                    @Override
                                    public void onFingerprintAuthFailed(@NotNull String s) {

                                    }

                                    @Override
                                    public void onCancel() {
                                        PrefManager.setAuthenticationType("");
                                        startActivity(new Intent(SplashScreenActivity.this,
                                                LoginActivity.class));
                                        SplashScreenActivity.this.finish();
                                    }
                                });
                        fpdialog.show();
                    }
                }

            }


        }
        //finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (PrefManager.getAuthenticationType()
                .equalsIgnoreCase("fpauth")) {
            fpdialog.dismiss();
            FPAuthDialog.create(this)
                    .setTitle("Login to Mifos X")
                    .setMessage("Use Fingerprint to access app")
                    .setCallback(new FPAuthCallback() {
                        @Override
                        public void onBelowAndroidMarshmallow() {
                            Intent intent = new Intent(SplashScreenActivity.this,
                                    PassCodeActivity.class);
                            intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
                            startActivity(intent);
                        }

                        @Override
                        public void onNoFingerprintScannerAvailable() {
                            Intent intent = new Intent(SplashScreenActivity.this,
                                    PassCodeActivity.class);
                            intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
                            startActivity(intent);
                        }

                        @Override
                        public void onNoFingerprintRegistered() {
                            Intent intent = new Intent(SplashScreenActivity.this,
                                    PassCodeActivity.class);
                            intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
                            startActivity(intent);
                        }

                        @Override
                        public void onFingerprintAuthSuccess(@NotNull String s) {
                            startActivity(new Intent(SplashScreenActivity.this,
                                    DashboardActivity.class));
                            SplashScreenActivity.this.finish();
                        }

                        @Override
                        public void onFingerprintAuthFailed(@NotNull String s) {

                        }

                        @Override
                        public void onCancel() {
                            PrefManager.setAuthenticationType("");
                            startActivity(new Intent(SplashScreenActivity.this,
                                    LoginActivity.class));
                            SplashScreenActivity.this.finish();
                        }
                    }).show();
        }

    }
}


