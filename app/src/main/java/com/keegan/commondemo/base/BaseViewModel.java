package com.keegan.commondemo.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends AndroidViewModel {


    private Application mApplication;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
    }

    public Application getApplication(){
        return mApplication;
    }


}

