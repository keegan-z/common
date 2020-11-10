package com.keegan.commondemo.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.keegan.common.permissionutil.PermissionListener;
import com.keegan.common.permissionutil.PermissionUtil;
import com.kongzue.dialog.interfaces.OnBackClickListener;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.trello.rxlifecycle3.components.support.RxFragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.jessyan.autosize.internal.CustomAdapt;

public abstract class BaseFragment extends RxFragment implements IBaseView, CustomAdapt {



    protected abstract @LayoutRes int setLayout();

    private PermissionListener permissionListener = null;


    protected abstract void initViews(@Nullable Bundle savedInstanceState);


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mRoot = inflater.inflate(setLayout(), container, false);

        initViews(savedInstanceState);

        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //在View创建完毕之后，isViewCreate 要变为true
        isViewCreated = true;
        if (!isHidden() && getUserVisibleHint())
            dispatchVisibleState(true);


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //因为只有在Fragment的View已经被创建的前提下，UI处理才有意义，所以
        if (isViewCreated) {
            //为了逻辑严谨，必须当目前状态值和目标相异的时候，才去执行UI可见分发
            if (currentVisibleState && !isVisibleToUser) {
                dispatchVisibleState(false);
            } else if (!currentVisibleState && isVisibleToUser) {
                dispatchVisibleState(true);
            }
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            dispatchVisibleState(false);
        } else {
            dispatchVisibleState(true);
        }
    }

    // ````前方高能````
    // ************************* 懒加载机制核心标志位 *****************************
    private boolean isViewCreated = false;//View是否已经被创建出来， View的创建是在onCreateView中进行，
    // ViewPager+Fragment很可能会发生多次destroy.create view的情况，所以这个标志位，表示当前fragment的view是否存在

    private boolean isFirstVisible = true;//当前Fragment是否是首次可见；
    // 设计这个Flag是 因为 根据懒加载的需求，我们往往需要在fragment首次可见的时候，整个页面全部初始化包括数据请求，
    // 而非第一次可见，UI内容的调整力度会小很多，所以还是区分出来比较好

    private boolean currentVisibleState = false;//当前可见状态
    // 它表示，当前UI是否可见。为什么设计这个标志位？
    // 因为，fragment本身api提供的的可见状态，并不完全可信。
    // 到目前为止，fragment可见状态的相关api有   setUserVisibleHint（可由外部调用，这个方法和生命周期无关）
    // onHiddenChange（它是在使用Activity+Fragment+FragmentManager时，由FragmentManager改变标志位mHidden）
    // onResume onPause 这两个是生命周期函数
    // 因为情况太多，我们单独上述使用任何一个方法作为当前fragment可见的判定，都不合适，所以还是综合所有情况，自己设定一个标志位


    //OK，标志位设计完毕了，现在把标志位用起来
    //设计一个方法，统一分发Ui的可见状态, 当UI可见状态发生变化时，都通过这个方法去处理
    //至于，UI可见状态什么时候发生变化，就要分多种情况
    // 比如，ViewPager+Fragment，滑动的时候
    // 比如，ViewPager+Fragment，从其中一个Fragment跳转到另外的Activity，然后又回来

    /**
     * @param isVisible 目标，true变为可见，false。变为不可见
     */
    void dispatchVisibleState(boolean isVisible) {
        //为了兼容内嵌ViewPager的情况,分发时，还要判断父Fragment是不是可见
        if (isVisible && isParentInvisible()) {//如果当前可见，但是父容器不可见，那么也不必分发
            return;
        }
        if (isVisible == currentVisibleState) return;//如果目标值，和当前值相同，那就别费劲了
        currentVisibleState = isVisible;//更新状态值
        if (isVisible) {//如果可见
            //那就区分是第一次可见，还是非第一次可见
            if (isFirstVisible) {
                isFirstVisible = false;
                onFragmentFirstVisible();
            }
            onFragmentResume();
            dispatchChildVisibilityState(true);
        } else {
            onFragmentPause();
            dispatchChildVisibilityState(false);
        }
    }

    /**
     * 判断父Fragment是不是可见
     *
     * @return 可见 true， 不可见 false
     */
    private boolean isParentInvisible() {
        Fragment parent = getParentFragment();
        if (parent instanceof BaseFragment) {
            BaseFragment lz = (BaseFragment) parent;
            return !lz.currentVisibleState;
        }
        return false;// 默认可见
    }

    /**
     * @param isVisible
     */
    private void dispatchChildVisibilityState(boolean isVisible) {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> list = fragmentManager.getFragments();
        if (list != null) {
            for (Fragment fg : list) {
                if (fg instanceof BaseFragment
                        && !fg.isHidden() && fg.getUserVisibleHint()) {//  判断可见要双重判定，isHidden和getUserVisibleHint
                    ((BaseFragment) fg).dispatchVisibleState(isVisible);
                }
            }
        }
    }

    /**
     * 当第一次可见的时候(此方法，在View的一次生命周期中只执行一次)
     */
    protected void onFragmentFirstVisible() {
        Log.d("LazyBaseFragment", "onFragmentFirstVisible 第一次可见,进行当前Fragment全局变量初始化，不涉及到UI操作");
    }

    /**
     * 当fragment变成可见的时候(可能会多次)
     */
    protected void onFragmentResume() {
        Log.d("LazyBaseFragment", "onFragmentResume 执行网络请求以及，UI操作");
    }

    /**
     * 当fragment变成不可见的时候(可能会多次)
     */
    protected void onFragmentPause() {
        Log.d("LazyBaseFragment", "onFragmentPause 中断网络请求，UI操作=======");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        reset();
    }

    private void reset() {
        isViewCreated = false;
        isFirstVisible = true;//还原成初始值
    }


    @Override
    public void onResume() {
        super.onResume();
        //因为onResume可能会在跳转Activity的时候反复执行，但是不是每一次都需要执行true分发
        //存在一个情况。ViewPager+Fragment ，当tab1 滑到 tab2 时， tab3 会执行完整的生命周期 onCreate-onCreateView-onViewCreated-onStart-onResume 但是此时tab3并不是可见的，
        // 没有必要执行true分发
        if (!isFirstVisible) {//只有在不是第一次可见的时候，才进入逻辑,由于isFirstVisible默认是true，所以，第一次进入onResume不会执行true分发
            if (!isHidden() && !currentVisibleState && getUserVisibleHint())//没有隐藏，当前状态为不可见，系统的可见hint为true 同时满足
                // 这个会发生在 Activity1 中 是ViewPager+Fragment时，如果从某个Fragment跳转到activity2，再跳回Activity1，那么 Activity1中的多个Fragment会同时执行onResume，
                //但是不会所有的fragment都是可见的，所以我只需要对可见的Fragment进行true分发
                dispatchVisibleState(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (currentVisibleState && getUserVisibleHint()) {
            dispatchVisibleState(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void launchActivity(Class<? extends Activity> pClass, Bundle bundle, int resCode) {
        Intent jumpIntent = new Intent();
        if (bundle != null) {
            jumpIntent.putExtra("bundle", bundle);
        }
        jumpIntent.setClass(getActivity(), pClass);
        if (resCode == -1) {
            startActivity(jumpIntent);
        } else {
            startActivityForResult(jumpIntent, resCode);
        }
    }


    /**
     * 在需要检测权限处调用该方法，同时实现回调接口即可
     */
    public void checkPermission(int requestCode, @NonNull PermissionListener permissionListener, @NonNull String... permissions) {
        this.permissionListener = permissionListener;
        if (PermissionUtil.hasPermission(getActivity(), permissions)) {
            List<String> grantedList = new ArrayList<>();
            Collections.addAll(grantedList, permissions);
            permissionListener.onSucceed(requestCode, grantedList);
        } else {
            PermissionUtil.requestPermissions(this, requestCode, permissions);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionListener != null) {
            PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, permissionListener);
        }
    }

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }


    @Override
    public void showLoading(String message) {

        WaitDialog.show((AppCompatActivity) this.getActivity(), message).setOnBackClickListener(new OnBackClickListener() {
            @Override
            public boolean onBackClick() {
                WaitDialog.dismiss();
                return false;
            }
        });


    }

    @Override
    public void hideLoading() {
        WaitDialog.dismiss();


    }

    @Override
    public void showBottomMenu(String[] texts) {
        BottomMenu.show((AppCompatActivity) this.getActivity(), texts, new OnMenuItemClickListener() {
            @Override
            public void onClick(String text, int index) {
                onBottomMenuSelect(text, index);
            }
        });
    }

    @Override
    public void showBottomMenu(String title, String[] texts) {
        BottomMenu.show((AppCompatActivity) this.getActivity(), texts, new OnMenuItemClickListener() {
            @Override
            public void onClick(String text, int index) {
                onBottomMenuSelect(text, index);
            }
        }).setTitle(title);
    }


    @Override
    public void showMessage(String title, String message, String okText, String cancleText) {
        MessageDialog.show((AppCompatActivity) this.getActivity(), title, message, okText, cancleText)
                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        onMessageCallBack();
                        return false;
                    }
                });
    }

    public void onMessageCallBack() {

    }

    public void onBottomMenuSelect(String text, int index) {

    }




}
