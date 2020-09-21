package com.xm.scrolltest;

import android.content.Context;
import android.util.AttributeSet;
import androidx.core.widget.NestedScrollView;

/**
 * @author zhuxiaomei
 * email:  zhuxiaomei.meleny@bytedance.com
 * date:   2020/8/12
 */
public class MyScrollView extends NestedScrollView {
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
}
