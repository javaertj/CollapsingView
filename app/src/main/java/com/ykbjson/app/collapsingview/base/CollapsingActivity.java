package com.ykbjson.app.collapsingview.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.ykbjson.app.collapsingview.R;
import com.ykbjson.app.collapsingview.widget.CollapsingLayout;

import butterknife.ButterKnife;

/**
 * 包名：com.ykbjson.app.collapsingview.base
 * 描述：透明变化状态栏界面基类activity.
 * 创建者：yankebin
 * 日期：2016/10/8
 */
public abstract class CollapsingActivity extends AppCompatActivity implements CollapsingLayout.OnCollapsingCallback {
    public CollapsingLayout collapsingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //实现透明状态栏的关键
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        View contentView = getLayoutInflater().inflate(bindLayout(), null, false);
        setContentView(contentView);
        ButterKnife.bind(this, contentView);
        collapsingLayout = (CollapsingLayout) contentView.findViewWithTag(getString(R.string.tag_collapsingRoot));
        collapsingLayout.setCollapsingCallback(this);
        if (null != getIntent() && null != getIntent().getExtras()) {
            initParameters(getIntent().getExtras());
        }
        initView(contentView);
    }

    public abstract int bindLayout();

    public abstract void initView(View view);

    public abstract void initParameters(Bundle parameters);

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}