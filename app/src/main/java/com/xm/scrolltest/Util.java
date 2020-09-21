package com.xm.scrolltest;

import android.util.Log;

import java.util.ArrayList;

/**
 * @author zhuxiaomei
 * email:  zhuxiaomei.meleny@bytedance.com
 * date:   2020/8/18
 */
public class Util {
    public static final ArrayList<String> dataList = new ArrayList<>();

    static {
        for (int i = 0; i < 100; i++) {
            dataList.add(String.valueOf(i));
        }
    }
}
