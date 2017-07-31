package br.com.acmestore.user.useradd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.ArrayList;

import br.com.acmestore.Constants;
import br.com.acmestore.Injection;
import br.com.acmestore.R;
import br.com.acmestore.MainActivity;
import br.com.acmestore.view.LoaderDialog;
import br.com.acmestore.data.entity.Product;
import br.com.acmestore.data.entity.User;
import br.com.acmestore.data.service.UserServiceApi;

public class UserAddActivity extends AppCompatActivity {

    private UserServiceApi mApi;
    private boolean isCoyote;

    private EditText edUserAddEmail;

    private LoaderDialog mLoaderDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useradd);

        mApi = Injection.provideUserServiceApiImpl();
        isCoyote = true;

        RadioButton rbUserAddCoyote = (RadioButton) findViewById(R.id.rb_useradd_coyote);
        rbUserAddCoyote.setChecked(true);
        rbUserAddCoyote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        RadioButton rbUserAddRoadRunner = (RadioButton) findViewById(R.id.rb_useradd_roadrunner);
        rbUserAddRoadRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });

        edUserAddEmail = (EditText) findViewById(R.id.et_useradd_email);
        edUserAddEmail.setOnFocusChangeListener(new FocusListener(R.id.et_useradd_email));

        Button btUserAddCreate = (Button) findViewById(R.id.bt_useradd_create);
        btUserAddCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edUserAddEmail.getText().toString();
                String type = isCoyote ? "COYOTE" : "ROADRUNNER";
                Double money = 100.000d;

                if (isFormValid(email)) {

                    mLoaderDialog = new LoaderDialog(UserAddActivity.this);
                    mLoaderDialog.setIndeterminate(true);
                    mLoaderDialog.setCancelable(false);
                    mLoaderDialog.show();

                    User user = new User(email, type, money, new ArrayList<Product>());
                    mApi.create(user, new UserServiceApi.UserCallback<User>() {
                        @Override
                        public void onLoad(User data) {
                            Intent intent = new Intent(UserAddActivity.this, MainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_USER, data);

                            mLoaderDialog.dismiss();

                            startActivity(intent);
                            UserAddActivity.this.finish();
                        }
                    });
                }
            }
        });
    }

    private void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rb_useradd_coyote:
                if (checked) {
                    isCoyote = true;
                }
                break;
            case R.id.rb_useradd_roadrunner:
                if (checked) {
                    isCoyote = false;
                }
                break;
            default:
                break;
        }
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
