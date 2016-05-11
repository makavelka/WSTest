package com.example.wheelytest.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.wheelytest.App;
import com.example.wheelytest.R;
import com.example.wheelytest.presenter.AuthPresenterImpl;
import com.example.wheelytest.view.AuthView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends BaseActivity implements AuthView{

    @Bind(R.id.login_editText_authActivity)
    EditText mLogin;
    @Bind(R.id.pass_editText_authActivity)
    EditText mPass;

    @Inject
    AuthPresenterImpl mPresenter;
    @Inject
    EventBus mEventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        App.getComponent().inject(this);
        mPresenter.onCreate(savedInstanceState, this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @OnClick(R.id.auth_button_authActivity)
    public void authorization() {
        mPresenter.authorize(mLogin.getText().toString(), mPass.getText().toString());
    }

    @Override
    public void showError(boolean isLogin) {
        if (isLogin) {
            mLogin.setError(getString(R.string.login_need_first_char_a));
        } else {
            mPass.setError(getString(R.string.pass_need_first_char_a));
        }
    }

    @Override
    public void startMapActivity() {
        startActivity(new Intent(this, MapActivity.class));
    }

    @Override
    public void restoreEditState(String login, String pass) {
        if (login != null) {
            mLogin.setText(login);
        }
        if (mPass != null) {
            mPass.setText(pass);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        onStopEventBus();
    }

    @Override
    public void onStopEventBus() {
        mEventBus.unregister(this);
    }
}
