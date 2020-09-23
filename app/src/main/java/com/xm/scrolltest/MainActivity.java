package com.xm.scrolltest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyScrollView parentView = findViewById(R.id.parent_view);
        TextView titleView = findViewById(R.id.tv_title);
        RecyclerView childView = findViewById(R.id.child_view);

        parentView.setTitleView(titleView);
        parentView.setChildView(childView);
    }
}