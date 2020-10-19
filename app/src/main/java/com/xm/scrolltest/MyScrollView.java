package com.xm.scrolltest;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.ViewCompat;

/**
 * @author zhuxiaomei
 * email:  zhuxiaomei.meleny@bytedance.com
 * date:   2020/8/12
 */

// TODO: zhuxiaomei 让标题拦截滑动, 并把滑动事件传递给MyView
public class MyScrollView extends FrameLayout {
    private static final String TAG = "MyScrollView";

    View childView;
    View parentView;

    View titleView;

    private int titleLeft;
    private int titleHeight;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //empty
    }

    public void setTitleView(View titleView) {
        this.titleView = titleView;
    }

    public void setChildView(View childView) {
        this.childView = childView;
        parentView = this;
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) return false;

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();

        return left <= x && x <= right && top <= y && y <= bottom;
    }

    int lastMotionY;

    public void setLastMotionY(int lastMotionY) {
        this.lastMotionY = lastMotionY;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int y = (int) ev.getY();
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setLastMotionY(y);
                break;
            case MotionEvent.ACTION_MOVE:
                setLastMotionY(y);
                return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final int y = (int) event.getY();
        Log.d(TAG, "onTouchEvent: y = " + y);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setLastMotionY(y);
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = lastMotionY - y;
                setLastMotionY(y);
                scroll(dy);
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //添加title的高度
        View title = ((ViewGroup) getChildAt(0)).getChildAt(0);
        titleLeft = titleHeight = title.getMeasuredHeight();
        int height = MeasureSpec.getSize(heightMeasureSpec) + titleLeft;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    private void scroll(int dy) {
        if (dy > 0) {
            if (dy < titleLeft) {
                parentView.scrollBy(0, dy);
                titleLeft -= dy;
            } else {
                parentView.scrollBy(0, titleLeft);
                dy -= titleLeft;
                childView.scrollBy(0, dy);
                titleLeft = 0;
            }
        } else {
            if (childView.canScrollVertically(dy)) {
                childView.scrollBy(0, dy);
            } else {
                int leftDistance = titleHeight - titleLeft;
                if (-dy > leftDistance) {
                    parentView.scrollBy(0, -leftDistance);
                    titleLeft = titleHeight;
                } else {
                    parentView.scrollBy(0, dy);
                    titleLeft -= dy;
                }
            }
        }
        Log.d(TAG, "scroll: parent.scrollY = " + parentView.getScrollY() + "child.scrollY = " + childView.getScrollY());
    }
}
