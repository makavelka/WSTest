package com.example.wheelytest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity {

    @Bind(R.id.login_editText_authActivity)
    EditText mLogin;
    @Bind(R.id.pass_editText_authActivity)
    EditText mPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.auth_button_authActivity)
    public void authorization() {

    }
}
