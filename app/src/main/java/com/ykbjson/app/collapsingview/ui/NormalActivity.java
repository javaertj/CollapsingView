package com.ykbjson.app.collapsingview.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drivingassisstantHouse.library.widget.linear.LinearListView;
import com.ykbjson.app.collapsingview.R;
import com.ykbjson.app.collapsingview.adapter.ListAdapter;
import com.ykbjson.app.collapsingview.base.CollapsingActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 包名：com.ykbjson.app.collapsingview.ui
 * 描述：不透明状态栏
 * 创建者：yankebin
 * 日期：2016/10/8
 */

public class NormalActivity extends CollapsingActivity {
    @Bind(R.id.listView)
    LinearListView listView;
    @Bind(R.id.iv_header_bg)
    ImageView ivHeaderBg;
    @Bind(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @Bind(R.id.group_title_left)
    LinearLayout groupTitleLeft;
    @Bind(R.id.tv_title_left)
    TextView tvTitleLeft;
    @Bind(R.id.iv_title_right)
    ImageView ivTitleRight;
    @Bind(R.id.group_title_right)
    LinearLayout groupTitleRight;
    @Bind(R.id.iv_title_right1)
    ImageView ivTitleRight1;
    @Bind(R.id.group_title_right1)
    LinearLayout groupTitleRight1;
    @Bind(R.id.iv_header_vertical_line)
    ImageView ivHeaderVerticalLine;
    @Bind(R.id.group_bus_route_header)
    FrameLayout groupBusRouteHeader;

    @OnClick(R.id.group_title_left)
    void back(){
        onBackPressed();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_normal;
    }

    @Override
    public void initView(View view) {
        tvTitleLeft.setText("我不要透明状态栏");
        ivHeaderBg.setImageResource(R.color.sip_red);
        groupTitleLeft.setVisibility(View.VISIBLE);
        ivTitleLeft.setImageResource(R.drawable.back_icon_white);
        listView.setAdapter(new ListAdapter(this,MainActivity.listData,R.layout.item_list));
    }

    @Override
    public void initParameters(Bundle parameters) {

    }

    @Override
    public void onCollapsing(float t, float coefficient) {

    }
}
