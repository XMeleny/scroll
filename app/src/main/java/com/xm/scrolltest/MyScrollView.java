package com.xm.scrolltest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author zhuxiaomei
 * email:  zhuxiaomei.meleny@bytedance.com
 * date:   2020/8/12
 */

public class MyScrollView extends FrameLayout {
    private static final String TAG = "MyScrollView";

    RecyclerView childView;
    View parentView;

    View titleView;

    private int titleLeft;
    private int titleHeight;

    private int mTouchSlop;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parentView = this;
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    public void setTitleView(View titleView) {
        this.titleView = titleView;
    }

    public void setChildView(RecyclerView childView) {
        this.childView = childView;
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
                if (Math.abs(dy) > mTouchSlop) {
                    setLastMotionY(y);
                    scroll(dy);
                }
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
        // 向下滑动的时候, 先考虑title(parent), 后考虑child;
        // 向上滑动的时候, 先考虑child, 后考虑parent. parent滑动时不能过多.
        if (dy > 0) {
            scrollChild(scrollParent(dy));
        } else {
            scrollParent(scrollChild(dy));
        }
    }

    private int scrollChild(int dy) {
        if (dy > 0) {
            childView.scrollBy(0, dy);
            dy -= dy;
        } else {
            int childLeft = childView.computeVerticalScrollOffset();//childLeft>=0
            if (-dy > childLeft) {
                childView.scrollBy(0, -childLeft);
                dy += childLeft;
            } else {
                childView.scrollBy(0, dy);
                dy -= dy;
            }
        }
        return dy;
    }

    private int scrollParent(int dy) {
        if (dy > 0) {
            if (dy < titleLeft) {
                parentView.scrollBy(0, dy);
                titleLeft -= dy;
                dy -= dy;
            } else {
                parentView.scrollBy(0, titleLeft);
                titleLeft -= titleLeft;
                dy -= titleLeft;
            }
        } else {
            int leftDistance = titleHeight - titleLeft;
            if (-dy > leftDistance) {
                parentView.scrollBy(0, -leftDistance);
                titleLeft += leftDistance;
            } else {
                parentView.scrollBy(0, dy);
                titleLeft -= dy;
            }
            dy -= dy;
        }
        return dy;
    }
}
