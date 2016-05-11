package com.example.wheelytest.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.wheelytest.view.BaseView;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    @Override
    public void showToast(String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(BaseActivity.this, resId, Toast.LENGTH_LONG).show();
    }
}
