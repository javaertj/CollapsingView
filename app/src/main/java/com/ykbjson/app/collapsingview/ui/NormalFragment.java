package com.ykbjson.app.collapsingview.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drivingassisstantHouse.library.widget.linear.LinearListView;
import com.ykbjson.app.collapsingview.R;
import com.ykbjson.app.collapsingview.adapter.ListAdapter;
import com.ykbjson.app.collapsingview.base.CollapsingFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 包名：com.ykbjson.app.collapsingview.ui
 * 描述：不透明状态栏fragment
 * 创建者：yankebin
 * 日期：2016/10/8
 */

public class NormalFragment extends CollapsingFragment {
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

    @OnClick(R.id.group_title_left)
    void back(){
        mContext.onBackPressed();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_normal;
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
        tvTitleLeft.setText("我不要透明状态栏-fragment");
        ivHeaderBg.setImageResource(R.color.sip_red);
        groupTitleLeft.setVisibility(View.VISIBLE);
        ivTitleLeft.setImageResource(R.drawable.back_icon_white);
        listView.setAdapter(new ListAdapter(mContext,MainActivity.listData,R.layout.item_list));
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void onCollapsing(float t, float coefficient) {

    }
}
