package com.ykbjson.app.collapsingview.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drivingassisstantHouse.library.widget.linear.LinearListView;
import com.nineoldandroids.view.ViewHelper;
import com.ykbjson.app.collapsingview.R;
import com.ykbjson.app.collapsingview.adapter.ListAdapter;
import com.ykbjson.app.collapsingview.base.CollapsingFragment;

import butterknife.Bind;

/**
 * 包名：com.ykbjson.app.collapsingview.ui
 * 描述：透明状态栏fragment
 * 创建者：yankebin
 * 日期：2016/10/8
 */

public class TranslucentStatusFragment extends CollapsingFragment {
    @Bind(R.id.iv_product_image)
    ImageView ivProductImage;
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

    @Override
    public int bindLayout() {
        return R.layout.activity_translucent_status;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView(View view) {
        //这里要设置这些的原因是header布局里基础设置的背景色这些，因为那个布局不止这一个activity引用
        ViewHelper.setAlpha(ivHeaderVerticalLine, 0.0f);
        ViewHelper.setAlpha(ivHeaderBg, 0.0f);
        groupTitleRight1.setVisibility(View.VISIBLE);
        tvTitleLeft.setText("透明状态栏哦-fragment");

        listView.setAdapter(new ListAdapter(mContext,MainActivity.listData,R.layout.item_list));
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void onCollapsing(float t, float coefficient,float flutterCoefficient) {
        //状态栏替代品
        ViewHelper.setAlpha(collapsingLayout.getFillingStatusView(), coefficient);
        ViewHelper.setAlpha(ivHeaderVerticalLine, coefficient);
        ViewHelper.setAlpha(ivHeaderBg, coefficient);
        int color;
        if (coefficient >= 0.5) {
            color = getResources().getColor(R.color.sip_gray_dark);
            ivTitleRight1.setImageResource(R.drawable.bus_gray_search);
        } else {
            color = getResources().getColor(R.color.white);
            ivTitleRight1.setImageResource(R.drawable.bus_route_search);
        }
        tvTitleLeft.setTextColor(color);
        ivTitleRight1.setAlpha(flutterCoefficient);
    }
}
