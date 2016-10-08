package com.ykbjson.app.collapsingview.utils;

import android.content.res.Resources;

/**
 * 包名：com.ykbjson.app.collapsingview.utils
 * 描述：工具类
 * 创建者：yankebin
 * 日期：2016/10/8
 */

public class Util {
    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    public static int getStatusBarHeight() {
        Resources res = Resources.getSystem();
        int resId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return res.getDimensionPixelSize(resId);
        }
        return 0;
    }
}
