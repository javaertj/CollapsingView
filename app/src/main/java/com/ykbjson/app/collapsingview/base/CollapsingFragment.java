package com.ykbjson.app.collapsingview.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.drivingassisstantHouse.library.base.IBaseFragment;
import com.ykbjson.app.collapsingview.R;
import com.ykbjson.app.collapsingview.widget.CollapsingLayout;

import butterknife.ButterKnife;

/**
 * 包名：com.ykbjson.app.collapsingview.base
 * 描述：透明变化状态栏界面基类fragment
 * 创建者：yankebin
 * 日期：2016/10/8
 */
public abstract class CollapsingFragment extends Fragment implements IBaseFragment, CollapsingLayout.OnCollapsingCallback {
    public CollapsingLayout collapsingLayout;
    public View mContentView;
    /**
     * 依附的Activity
     **/
    protected AppCompatActivity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mContentView) {
            mContentView = inflater.inflate(bindLayout(), container, false);
            collapsingLayout = (CollapsingLayout) mContentView.findViewWithTag(getResources().getString(R.string.tag_collapsingRoot));
            //初始化参数
            initParms(getArguments());
            ButterKnife.bind(this, mContentView);
            // 控件初始化
            initView(mContentView);
            // 业务处理
            doBusiness(mContext);
            collapsingLayout.setCollapsingCallback(this);
        } else {
            ButterKnife.bind(this, mContentView);
        }
        return mContentView;
    }

    @Override
    public void onNetWorkChanged(boolean connected) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);//接除ButterKnife注解
        if (mContentView != null && mContentView.getParent() != null) {
            ((ViewGroup) mContentView.getParent()).removeView(mContentView);
        }
    }

    /**
     * 获取id对应的view
     *
     * @param id  view id
     * @param <T> view类型
     * @return id对应的view
     */
    public <T extends View> T get(@IdRes int id) {
        return (T) collapsingLayout.findViewById(id);
    }
}