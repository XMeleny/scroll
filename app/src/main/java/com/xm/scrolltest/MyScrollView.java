package com.xm.scrolltest;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
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

//extends ScrollView implements NestedScrollingParent3
public class MyScrollView extends FrameLayout implements NestedScrollingParent3 {
    private static final String TAG = "MyScrollView";

    View childView;
    View parentView;

    View titleView;

    private int mAxes;
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

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {

    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mAxes = axes;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mAxes = SCROLL_AXIS_NONE;
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //添加title的高度
        View title = ((ViewGroup) getChildAt(0)).getChildAt(0);
        titleLeft = titleHeight = title.getMeasuredHeight();
        int height = MeasureSpec.getSize(heightMeasureSpec) + titleLeft;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    // COMMENT: zhuxiaomei 在滑动的时候, 假如触摸点在title处, 不会执行到这个方法.因为这个方法是NestedChild向上找NestedParent才调用的. titleView不是NestedChild不会进行寻找, 所以不会执行
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        scroll(childView.getTop(), dy, consumed);
    }

    // COMMENT: zhuxiaomei 人的视角: if(dy>0) 向下滑动, 否则向上滑动
    private void scroll(int childTop, int dy, int[] consumed) {
        if (dy > 0) {
            if (dy <= titleLeft) {
                parentView.scrollBy(0, dy);
                titleLeft -= dy;
                consumed[1] = dy;
            } else {
                parentView.scrollBy(0, titleLeft);
                consumed[1] = titleLeft;
                titleLeft = 0;
            }
        } else {
            if (childView.canScrollVertically(dy)) {
                //do nothing, let the child to deal with it
            } else {
                // COMMENT: zhuxiaomei 如果dy比title大, 那么title只能滑剩下的.
                int leftDistance = titleHeight - titleLeft;
                if (-dy > leftDistance) {
                    parentView.scrollBy(0, -leftDistance);
                    titleLeft = titleHeight;
                    consumed[1] = dy;

                } else {
                    parentView.scrollBy(0, dy);
                    titleLeft -= dy;
                    consumed[1] = dy;
                }
            }
            /*
            if(child.canScroll(dy)){
                doNothing(); left the child to do
            }else{
                if(-dy > titleHeight){
                    if(titleLeft<titleHeight){
                        parent.scrollBy(0,-titleHeight);
                        titleLeft = titleHeight;
                        consumed[1]=dy;
                    } else {
                        consumed[1]=dy;
                    }
                }else{
                    parent.scrollBy(dy);
                    titleLeft -= dy;

             */
        }

//        onChildScrolling(childTop, dy, consumed);
    }

    // COMMENT: zhuxiaomei scrollBy 滑动的是内部视图?

    // COMMENT: zhuxiaomei 滑动的时候childTop不会改变...
    private void onParentScrolling(int childTop, int dy, int[] consumed) {
        if (childTop == 0) {
            if (dy > 0) {
                childView.scrollBy(0, dy);
                consumed[1] = dy;
            } else if (childView.canScrollVertically(dy)) {
                childView.scrollBy(0, dy);
                consumed[1] = dy;
            }
        } else {
            if (childTop < dy) {
                consumed[1] = dy - childTop;
            }
        }
    }

    private void onChildScrolling(int childTop, int dy, int[] consumed) {
        if (childTop <= 0) {
            if (dy < 0) {
                if (!childView.canScrollVertically(dy)) {
                    consumed[1] = dy;
                    this.scrollBy(0, dy);
                }
            }
        } else {
            if (dy < 0 || childTop > dy) {
                consumed[1] = dy;
                this.scrollBy(0, dy);
            } else {
                consumed[1] = dy;
                this.scrollBy(0, childTop);
            }
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mAxes;
    }
}
