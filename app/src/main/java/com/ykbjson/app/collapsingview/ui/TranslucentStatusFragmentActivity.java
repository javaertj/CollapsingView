package com.ykbjson.app.collapsingview.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.ykbjson.app.collapsingview.R;

/**
 * 包名：com.ykbjson.app.collapsingview.ui
 * 描述：透明状态栏fragment的容器
 * 创建者：yankebin
 * 日期：2016/10/8
 */

public class TranslucentStatusFragmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //实现透明状态栏的关键
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                Fragment.instantiate(this,TranslucentStatusFragment.class.getName())).commit();
    }
}
