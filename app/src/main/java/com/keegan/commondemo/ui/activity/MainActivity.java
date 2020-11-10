package com.keegan.commondemo.ui.activity;

import androidx.databinding.DataBindingUtil;

import com.keegan.commondemo.R;
import com.keegan.commondemo.base.BaseActivity;
import com.keegan.commondemo.databinding.ActivityMainBinding;
import com.keegan.commondemo.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding activityMainBinding;
    private MainViewModel mainViewModel;

    @Override
    protected void initViewModel() {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = mViewModelProvider.get(MainViewModel.class);
        activityMainBinding.setMainViewModel(mainViewModel);
        activityMainBinding.setLifecycleOwner(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {
    }


}