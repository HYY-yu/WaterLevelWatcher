package com.app.feng.waterlevelwatcher.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.UserBean;
import com.app.feng.waterlevelwatcher.network.BaseSubscriber;
import com.app.feng.waterlevelwatcher.network.RetrofitManager;
import com.app.feng.waterlevelwatcher.network.bean.ResponseBean;
import com.app.feng.waterlevelwatcher.network.interfaces.LoginService;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox autoLogin;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        realm = Realm.getDefaultInstance();

        ////  判断是否自动登陆
        isAutoLoginEnable();


        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView,int id,KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        autoLogin = (CheckBox) findViewById(R.id.auto_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void isAutoLoginEnable() {
        UserBean userBean = realm.where(UserBean.class)
                .findFirst();

        if (userBean != null && userBean.autoLogin) {
            jumpToMain();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText()
                .toString();
        String password = mPasswordView.getText()
                .toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            startLoginService(username,password);
        }
    }

    private void startLoginService(final String username,final String password) {
        RetrofitManager.getRetrofit()
                .create(LoginService.class)
                .login(username,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<UserBean>(getApplicationContext()) {

                    @Override
                    public void onStartLoad() {
                        showProgress(true);
                    }

                    @Override
                    public void onEndLoad() {
                        showProgress(false);
                    }

                    @Override
                    public void onSuccess(
                            ResponseBean<UserBean> responseBean) {
                        //登陆成功
                        realm.beginTransaction();
                        UserBean userBean = new UserBean();
                        userBean.setUsername(username);
                        userBean.setPassword(" ");
                        userBean.setDisplayName(responseBean.getDatas()
                                                        .get(0)
                                                        .getDisplayName());
                        userBean.autoLogin = false;

                        if (autoLogin.isChecked()) {
                            userBean.autoLogin = true;
                        }
                        realm.copyToRealmOrUpdate(userBean);
                        realm.commitTransaction();

                        jumpToMain();
                    }

                    @Override
                    public void onFail(
                            ResponseBean<UserBean> responseBean) {
                        super.onFail(responseBean);
                        //失败
                        Logger.i("LoginActivity -- " + responseBean.getMessage());
                        mPasswordView.setError(responseBean.getMessage());
                        mPasswordView.requestFocus();
                    }
                });

    }

    private boolean isUsernameValid(String username) {
        //   用户名无要求
        return true;
    }

    private boolean isPasswordValid(String password) {
        //   密码大于一位
        return password.length() >= 1;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate()
                .setDuration(shortAnimTime)
                .alpha(show ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate()
                .setDuration(shortAnimTime)
                .alpha(show ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
    }

    private void jumpToMain() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (realm != null) {
            realm.close();
        }
    }
}

