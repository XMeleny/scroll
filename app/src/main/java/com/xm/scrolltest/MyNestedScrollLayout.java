package com.xm.scrolltest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author zhuxiaomei
 * email:  zhuxiaomei.meleny@bytedance.com
 * date:   2020/9/21
 */
public class MyNestedScrollLayout extends FrameLayout {
    public MyNestedScrollLayout(@NonNull Context context) {
        super(context);
    }

    public MyNestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //tab layout + viewpager
    private View ChildView;

    private RecyclerView rootList;
    private RecyclerView childList;


}
