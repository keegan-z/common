package com.keegan.commondemo.ui.activity;

import androidx.databinding.DataBindingUtil;

import android.view.View;

import com.keegan.commondemo.R;
import com.keegan.commondemo.base.BaseActivity;
import com.keegan.commondemo.databinding.ActivityLoginBinding;
import com.keegan.commondemo.viewmodel.LoginViewModel;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding activityLoginBinding;
    private LoginViewModel loginViewModel;

    @Override
    protected void initViewModel() {
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = mViewModelProvider.get(LoginViewModel.class);
        activityLoginBinding.setLoginViewModel(loginViewModel);
        activityLoginBinding.setLifecycleOwner(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }


    public void login(View view) {
        loginViewModel.login();
    }
}