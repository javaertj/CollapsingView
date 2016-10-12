/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ykbjson.app.collapsingview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * 包名：com.ykbjson.app.collapsingview.widget
 * 描述：自定义scrollview，为了向外部提供onScrollChanged方法的回调参数
 * 创建者：yankebin
 * 日期：2016/10/8
 */
public class ObservableScrollView extends ScrollView {
    private ArrayList<Callbacks> mCallbacks = new ArrayList<>();

    public ObservableScrollView(Context context) {
        this(context, null);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(OVER_SCROLL_NEVER);
        requestDisallowInterceptTouchEvent(true);
    }

    //解决子view没超出屏幕时不能缩放的问题
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_MOVE) {
            return onTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        handleOnScrollCallback(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mCallbacks.isEmpty()) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    handleOnDownMotionEvent();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    handleOnUpOrCancelMotionEvent();
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    private void handleOnDownMotionEvent() {
        if (checktock()) {
            return;
        }
        for (Callbacks callbacks : mCallbacks) {
            callbacks.onDownMotionEvent();
        }
    }

    private void handleOnUpOrCancelMotionEvent() {
        if (checktock()) {
            return;
        }
        for (Callbacks callbacks : mCallbacks) {
            callbacks.onUpOrCancelMotionEvent();
        }
    }

    private void handleOnScrollCallback(int l, int t, int oldl, int oldt) {
        if (checktock()) {
            return;
        }
        for (Callbacks callbacks : mCallbacks) {
            callbacks.onScrollCallback(l, t, oldl, oldt);
        }
    }

    private boolean checktock() {
        return mCallbacks.isEmpty();
    }

    public boolean addScrollCallbacks(Callbacks listener) {
        return !mCallbacks.contains(listener) && mCallbacks.add(listener);
    }

    public boolean removeScrollCallbacks(Callbacks listener) {
        return mCallbacks.contains(listener) && mCallbacks.remove(listener);
    }

    public interface Callbacks {
        public void onScrollCallback(int l, int t, int oldl, int oldt);

        public void onDownMotionEvent();

        public void onUpOrCancelMotionEvent();
    }
}
