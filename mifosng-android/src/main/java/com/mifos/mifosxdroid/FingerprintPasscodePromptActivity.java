package com.mifos.mifosxdroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.mokshmahajan.fpauthdialoglibrary.FpAuthCallback;
import com.github.mokshmahajan.fpauthdialoglibrary.FpAuthDialog;
import com.mifos.mifosxdroid.login.LoginActivity;
import com.mifos.mifosxdroid.online.DashboardActivity;
import com.mifos.mifosxdroid.passcode.PassCodeActivity;
import com.mifos.mobile.passcode.utils.PassCodeConstants;
import com.mifos.utils.PrefManager;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FingerprintPasscodePromptActivity extends AppCompatActivity {
    @BindView(R.id.rg_options)
    RadioGroup rgOptions;

    private FpAuthDialog fpAuthDialog;

    RadioButton selectedRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_passcode_prompt);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_proceed)
    void proceed() {
        if (selectedRadioButton == null) {
            Toast.makeText(this, "Please select an option first",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (selectedRadioButton.getText().toString().equalsIgnoreCase("passcode")) {
                //User has opted for Passcode
                PrefManager.setAuthType("Passcode");
                Intent intent = new Intent(FingerprintPasscodePromptActivity.this,
                        PassCodeActivity.class);
                intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
                startActivity(intent);
                finish();
            } else {
                //User has opted for Fingerprint Authentication
                PrefManager.setAuthType("FpAuth");
                showFpAuthDialog();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fpAuthDialog != null) {
            fpAuthDialog.dismiss();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (PrefManager.getAuthType().equalsIgnoreCase("fpauth"))
            showFpAuthDialog();
    }

    public void checkButton(View view) {
        selectedRadioButton = findViewById(rgOptions.getCheckedRadioButtonId());
    }

    private void showFpAuthDialog() {
        fpAuthDialog = new FpAuthDialog(this);
        fpAuthDialog.setTitle("Proceed to Login")
                .setMessage("To continue touch the fingerprint sensor")
                .setCancelText("Use Password instead")
                .setCallback(new FpAuthCallback() {
                    @Override
                    public void onFpAuthSuccess() {
                        startActivity(new Intent(
                                FingerprintPasscodePromptActivity.this,
                                DashboardActivity.class));
                        FingerprintPasscodePromptActivity.this.finish();
                    }

                    @Override
                    public void onFpAuthFailed(@NotNull String s) {

                    }

                    @Override
                    public void onCancel() {
                        PrefManager.setAuthType("");
                        startActivity(new Intent(
                                FingerprintPasscodePromptActivity.this,
                                LoginActivity.class));
                        FingerprintPasscodePromptActivity.this.finish();
                    }
                });
        fpAuthDialog.show();
    }
}
