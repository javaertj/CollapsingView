package com.ykbjson.app.collapsingview.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;
import com.ykbjson.app.collapsingview.R;
import com.ykbjson.app.collapsingview.utils.Util;


/**
 * 包名：com.ykbjson.app.collapsingview.widget
 * 描述：透明状态栏视图
 * 创建者：yankebin
 * 日期：2016/10/8
 */

public class CollapsingLayout extends FrameLayout implements OnScrollListener, ObservableScrollView.Callbacks {
    /**
     * 渐变系数回调接口
     */
    public interface OnCollapsingCallback {
        /**
         * 渐变系数变化
         *
         * @param t           竖直方向的滚动距离
         * @param coefficient 计算出的渐变系数[0,1]
         */
        void onCollapsing(float t, float coefficient);
    }

    private final String TAG_SCROLL = getResources().getString(R.string.tag_collapsingScroll);
    private final String TAG_LIST = getResources().getString(R.string.tag_collapsingList);
    private final String TAG_SCALE = getResources().getString(R.string.tag_collapsingScale);
    private final String TAG_HEADER = getResources().getString(R.string.tag_collapsingHeader);
    private final String TAG_CONTENT = getResources().getString(R.string.tag_collapsingContent);
    private final String TAG_FLOAT = getResources().getString(R.string.tag_collapsingFloat);

    /**
     * 加载状态栏和header的容器
     */
    private LinearLayout headerLayout;
    /**
     * 伪装状态栏
     */
    private ImageView fillingStatusView;
    /**
     * 收缩系数回调接口
     */
    private OnCollapsingCallback collapsingCallback;
    /**
     * 防止多次加载header布局
     */
    private boolean isHandled;
    /**
     * 收缩系数的基础高度,默认为statusbar+titlebar的高度*2
     */
    private int collapsingHeight;
    /**
     * 手指在滚动视图上上一次记录的y值
     */
    private float lastMoveY;
    /**
     * 是在缩放
     */
    private boolean mScaling;
    /**
     * 缩放视图
     */
    private View scaleView;
    /**
     * 悬浮视图
     */
    private View floatView;
    /**
     * 悬浮视图顶部距离
     */
    private float floatViewTop;
    /**
     * 缩放视图初始宽度
     */
    private int scaleWidth;
    /**
     * 缩放视图初始高度
     */
    private int scaleHeight;


    private int statusBarColorRes;
    private float statusBarAlpha;
    private boolean needTranslucentStatus;

    private boolean isAnimation;

    public CollapsingLayout(Context context) {
        this(context, null);
    }

    public CollapsingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTag(getResources().getString(R.string.tag_collapsingRoot));
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CollapsingLayout);
        statusBarColorRes = typedArray.getResourceId(R.styleable.CollapsingLayout_statusBarColor,
                R.color.sip_gray_dark);
        statusBarAlpha = typedArray.getFloat(R.styleable.CollapsingLayout_statusBarAlpha, 0.0f);
        needTranslucentStatus = typedArray.getBoolean(
                R.styleable.CollapsingLayout_needTranslucentStatus, true);
        typedArray.recycle();
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed && !isHandled) {
            isHandled = true;
            //此处可以取到缩放视图的宽高，不在此处去替换原布局子view，解决header的布局不能正常显示的问题
            View scaleView = findViewWithTag(TAG_SCALE);
            if (null == scaleView) {
                return;
            }
            scaleWidth = scaleView.getMeasuredWidth();
            scaleHeight = scaleView.getMeasuredHeight();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //可以解决进入页面时布局错乱的问题
        handleLayout(getContext());
    }

    @Override
    protected void attachViewToParent(View child, int index, ViewGroup.LayoutParams params) {
        super.attachViewToParent(child, index, params);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        final float scrollY = getViewScrollY(view);
        final float coefficient = Math.min(scrollY * 1.0f / collapsingHeight, 1);
        handleScroll(scrollY, coefficient);
    }

    @Override
    public void onScrollCallback(int l, int t, int oldl, int oldt) {
        final float coefficient = Math.min(t * 1.0f / collapsingHeight, 1);
        handleScroll(t, coefficient);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent() {

    }


    /**
     * 重置放大的视图
     */
    private void restScaleView() {
        final ViewGroup.LayoutParams lp = scaleView.getLayoutParams();
        final float w = scaleView.getLayoutParams().width;// 图片当前宽度
        final float h = scaleView.getLayoutParams().height;// 图片当前高度
        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
                checkScaleViewAttr();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimation = false;
                checkScaleViewAttr();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - scaleWidth) * cVal);
                lp.height = (int) (h - (h - scaleHeight) * cVal);
                scaleView.setLayoutParams(lp);
            }
        });
        anim.start();
    }

    /**
     * 处理加载后的布局
     *
     * @param context 上下文
     */
    private void handleLayout(Context context) {
        View headerView = findViewWithTag(TAG_HEADER);
        if (null == headerView) {
            throw new IllegalArgumentException("can not find header view with tag @string/tag_collapsingHeader");
        }
        removeView(headerView);

        headerLayout = new LinearLayout(context);
        headerLayout.setOrientation(LinearLayout.VERTICAL);


        fillingStatusView = new ImageView(context);
        fillingStatusView.setBackgroundResource(statusBarColorRes);
        fillingStatusView.setAlpha(statusBarAlpha);

        headerLayout.addView(fillingStatusView, new LinearLayout.LayoutParams(-1, Util.getStatusBarHeight()));
        headerLayout.addView(headerView, headerView.getLayoutParams());
        addView(headerLayout, new LayoutParams(-1, -2));

        //sdk版本小于4.4不需要额外的状态栏
        if (!isSupport()) {
            fillingStatusView.setVisibility(GONE);
        }
        //强制测量以获取其高度
        headerLayout.measure(0, 0);
        setCollapsingHeight(headerLayout.getMeasuredHeight() << 1);

        View scrollView = findViewWithTag(TAG_SCROLL);
        View absListView = findViewWithTag(TAG_LIST);

        if (null != scrollView) {
            ((ObservableScrollView) scrollView).addScrollCallbacks(this);
            handleScale(scrollView);
        } else if (null != absListView) {
            ((AbsListView) absListView).setOnScrollListener(this);
            handleScale(absListView);
        }

        //contentview padding 兼容不需要沉浸的页面
        if (!needTranslucentStatus) {
            View content = findViewWithTag(TAG_CONTENT);
            if (null == content) {
                return;
            }
            content.setPadding(0, headerLayout.getMeasuredHeight(), 0, 0);
        }
    }

    /**
     * 处理缩放
     *
     * @param scrollView 处理touch事件的view
     */
    private void handleScale(final View scrollView) {
        scaleView = findViewWithTag(TAG_SCALE);
        if (null == scaleView) {
            return;
        }
        scrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isAnimation) {
                    return true;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (mScaling) {
                            //手指离开后恢复图片
                            resetScaleFlag();
                            restScaleView();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final int scrollY = getScrollY(scrollView);
                        if (!mScaling) {
                            if (lastMoveY == 0 && scrollY == 0) {
                                lastMoveY = event.getY();// 滚动到顶部时记录位置，否则正常返回
                                break;
                            }
                        }
                        // 当前位置比记录位置要小，正常返回
                        if (event.getY() - lastMoveY <= 0) {
                            resetScaleFlag();
                            //防止快速回退时没能减掉最后一部分位移距离
                            checkScaleViewAttr();
                            break;
                        }
                        int distance = (int) ((event.getY() - lastMoveY) * 0.6); // 滚动距离乘以一个系数
                        //滚动距离负数，不缩放
                        if (distance <= 0) {
                           resetScaleFlag();
                            break;
                        }
                        if (scrollY == 0) {
                            // 处理放大
                            handleScale(distance);
                            return true;
                        }// 返回true表示已经完成触摸事件，不再传递给子视图处理
                }
                return false;
            }
        });
    }

    /**
     * 重置缩放相关标志位参数，解决多次上下滑动时缩放距离不准确的问题
     */
    private void resetScaleFlag(){
        mScaling = false;
        lastMoveY = 0;
    }

    /***
     * 矫正缩放视图宽高，防止快速回退时没能减掉最后一部分位移距离
     */
    private void checkScaleViewAttr() {
        ViewGroup.LayoutParams scaleViewLayoutParams = scaleView.getLayoutParams();
        if (scaleViewLayoutParams.width != scaleWidth ||
                scaleViewLayoutParams.height != scaleHeight) {
            scaleViewLayoutParams.width = scaleWidth;
            scaleViewLayoutParams.height = scaleHeight;
            scaleView.setLayoutParams(scaleViewLayoutParams);
        }
    }

    /**
     * 处理放大
     *
     * @param distance 放大增量
     */
    private void handleScale(int distance) {
        ViewGroup.LayoutParams scaleViewLayoutParams = scaleView.getLayoutParams();
        mScaling = true;
        scaleViewLayoutParams.width = scaleWidth + distance;
        scaleViewLayoutParams.height = scaleHeight + distance;
        scaleView.setLayoutParams(scaleViewLayoutParams);
    }

    /**
     * 获取滚动视图滚动距离
     *
     * @param view 滚动视图
     * @return
     */
    private int getScrollY(View view) {
        if (null == view) {
            return -1;
        }
        if (view instanceof AbsListView) {
            return getViewScrollY((AbsListView) view);
        }
        return view.getScrollY();
    }

    /**
     * 设置渐变的高度基数
     *
     * @param collapsingHeight 高度基数
     */
    public void setCollapsingHeight(int collapsingHeight) {
        this.collapsingHeight = collapsingHeight;
    }

    /**
     * 获取AbsListView当前的滚动距离
     *
     * @param absListView AbsListView
     * @return 当前的滚动距离
     */
    private int getViewScrollY(AbsListView absListView) {
        View c = absListView.getChildAt(0);//第一个可见的view
        if (c == null) {
            return 0;
        }
        int top = c.getTop() + absListView.getPaddingTop();
        return -top;
    }

    /**
     * 处理滚动
     *
     * @param coefficient 滚动系数
     */
    private void handleScroll(float scrollY, float coefficient) {
        if (null != collapsingCallback) {
            collapsingCallback.onCollapsing(scrollY, coefficient);
        }
        //悬浮视图
        if (null == floatView) {
            floatView = findViewWithTag(TAG_FLOAT);
        }
        if (null != floatView) {
            if (floatViewTop == 0) {
                floatViewTop = getChildTop(floatView, floatView.getTop()) - headerLayout.getMeasuredHeight();
            }
            if (floatViewTop <= scrollY) {
                ViewHelper.setTranslationY(floatView, -floatViewTop + scrollY);
            } else {
                ViewHelper.setTranslationY(floatView, 0);
            }
        }
    }

    /**
     * 找到view距离顶部的距离
     *
     * @param view
     * @param height
     * @return
     */
    private int getChildTop(View view, int height) {
        ViewParent parent = view.getParent();
        //没测试过相等的情况，可以改为tag相等比较
        if (null == parent || this == parent) {
            return height;
        }
        ViewGroup parentView = (ViewGroup) parent;
        height += parentView.getTop();
        return getChildTop(parentView, height);
    }

    /**
     * 设置透明回调接口
     *
     * @param collapsingCallback 透明回调接口
     */
    public void setCollapsingCallback(OnCollapsingCallback collapsingCallback) {
        this.collapsingCallback = collapsingCallback;
    }

    /**
     * 设置状态栏背景颜色
     *
     * @param color 颜色值
     */

    public void setStatusBarColor(@ColorInt int color) {
        fillingStatusView.setBackgroundColor(color);

    }

    /**
     * 设置状态栏背景图片
     *
     * @param drawableRes 图片资源id
     */
    public void setStatusBarBackground(@DrawableRes int drawableRes) {
        fillingStatusView.setBackgroundResource(drawableRes);
    }

    /**
     * header布局
     *
     * @return header布局
     */
    public LinearLayout getHeaderView() {
        return headerLayout;
    }

    /**
     * 返回缩放视图
     *
     * @return
     */
    public View getScaleView() {
        return findViewWithTag(TAG_SCALE);
    }

    /**
     * statusbar的替代view
     *
     * @return statusbar的替代view
     */
    public ImageView getFillingStatusView() {
        return fillingStatusView;
    }

    public void setHeaderLayoutBgColor(@ColorRes int color) {
        headerLayout.setBackgroundResource(color);
    }

    public void setHeaderLayoutBgDrawable(@DrawableRes int drawable) {
        headerLayout.setBackgroundResource(drawable);
    }

    /**
     * 是否支持当前版本
     *
     * @return 是否支持
     */
    private boolean isSupport() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 是否沉浸了状态栏
     *
     * @return
     */
    private boolean isTranslucentStatus() {
        return isSupport() && needTranslucentStatus;
    }
}
