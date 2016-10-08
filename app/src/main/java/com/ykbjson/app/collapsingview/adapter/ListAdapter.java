package com.ykbjson.app.collapsingview.adapter;

import android.content.Context;
import android.widget.TextView;

import com.drivingassisstantHouse.library.base.BSimpleEAdapter;
import com.drivingassisstantHouse.library.base.SimpleAdapterHolder;
import com.ykbjson.app.collapsingview.R;

import java.util.List;

/**
 * 包名：com.ykbjson.app.collapsingview.adapter
 * 描述：
 * 创建者：yankebin
 * 日期：2016/10/8
 */

public class ListAdapter extends BSimpleEAdapter<String> {
    /**
     * @param context 上下文
     * @param datas   数据源
     * @param id
     */
    public ListAdapter(Context context, List<String> datas, int id) {
        super(context, datas, id);
    }

    @Override
    public void covertView(SimpleAdapterHolder holder, int positon, List<String> datas, Object obj) {
        TextView textView=holder.getView(R.id.item_textView);
        textView.setText(obj.toString());
    }
}
