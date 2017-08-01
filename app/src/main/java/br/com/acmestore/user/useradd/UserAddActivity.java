package br.com.acmestore.user.useradd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;

import br.com.acmestore.Constants;
import br.com.acmestore.Injection;
import br.com.acmestore.MainActivity;
import br.com.acmestore.R;
import br.com.acmestore.data.entity.Product;
import br.com.acmestore.data.entity.User;
import br.com.acmestore.data.service.UserServiceApi;
import br.com.acmestore.service.RegistrationIntentService;
import br.com.acmestore.view.LoaderDialog;

public class UserAddActivity extends AppCompatActivity {

    private BroadcastReceiver mBroadcastReceiver;

    private UserServiceApi mApi;
    private EditText edUserAddEmail;
    private LoaderDialog mLoaderDialog;

    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useradd);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().endsWith(RegistrationIntentService.REGISTRATION_SUCCESS)) {
                    userToken = intent.getStringExtra("token");

                } else {
                    //RegistrationIntentService.REGISTRATION_ERROR;
                    userToken = "";
                }
            }
        };

        //check status of google play in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (ConnectionResult.SUCCESS != resultCode) {
            //check type of error
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google play services is not install/enabled in this device!",
                        Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for google play service",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }


        mApi = Injection.provideUserServiceApiImpl();

        edUserAddEmail = (EditText) findViewById(R.id.et_useradd_email);
        edUserAddEmail.setOnFocusChangeListener(new FocusListener(R.id.et_useradd_email));

        Button btUserAddCreate = (Button) findViewById(R.id.bt_useradd_create);
        btUserAddCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edUserAddEmail.getText().toString();
                Double money = 100.000d;

                if (isFormValid(email)) {

                    if (isValidToken(userToken)) {
                        mLoaderDialog = new LoaderDialog(UserAddActivity.this);
                        mLoaderDialog.setIndeterminate(true);
                        mLoaderDialog.setCancelable(false);
                        mLoaderDialog.show();

                        User user = new User(email, money, userToken, new ArrayList<Product>());
                        mApi.create(user, new UserServiceApi.UserCallback<User>() {
                            @Override
                            public void onLoaded(User data) {
                                Intent intent = new Intent(UserAddActivity.this, MainActivity.class);
                                intent.putExtra(Constants.INTENT_KEY_USER, data);

                                mLoaderDialog.dismiss();

                                startActivity(intent);
                                UserAddActivity.this.finish();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid user token! This application will be closed",
                                Toast.LENGTH_LONG).show();

                        finish();
                    }
                }
            }
        });
    }

    private boolean isValidToken(String userToken) {
        return userToken != null && !userToken.isEmpty();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_SUCCESS));

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    private boolean isFormValid(String email) {
        if (null == email || email.isEmpty()) {
            edUserAddEmail.setError(getString(R.string.et_useradd_email_required));
            edUserAddEmail.requestFocus();
            return false;
        } else if (email.length() < 3) {
            edUserAddEmail.setError(getString(R.string.et_useradd_email_invalid));
            edUserAddEmail.requestFocus();
            return false;
        }
        return true;
    }

    private void showSoftKeyboard(View view, int id, boolean hasFocus) {
        if (view.getId() == id && !hasFocus) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    class FocusListener implements View.OnFocusChangeListener {
        private int mViewId;

        public FocusListener(int viewId) {
            this.mViewId = viewId;
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            showSoftKeyboard(view, mViewId, hasFocus);
        }
    }
}
