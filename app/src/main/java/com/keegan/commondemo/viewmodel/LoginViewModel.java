package com.keegan.commondemo.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.keegan.common.util.ToastUtil;
import com.keegan.commondemo.base.BaseViewModel;
import com.keegan.commondemo.entity.UserInfoResponse;

public class LoginViewModel extends BaseViewModel {

    public MutableLiveData<UserInfoResponse> userInfo = new MutableLiveData<>();

    public MutableLiveData<String> userName = new MutableLiveData<>();

    public MutableLiveData<String> password = new MutableLiveData<>();


    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void login() {

        if (TextUtils.isEmpty(userName.getValue())) {
            ToastUtil.show("登录名不能为空");
            return;
        }

        if (TextUtils.isEmpty(password.getValue())) {
            ToastUtil.show("登录密码不能为空");
            return;
        }

    }


}
