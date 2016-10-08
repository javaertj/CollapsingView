package com.ykbjson.app.collapsingview.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.drivingassisstantHouse.library.tools.SLog;
import com.ykbjson.app.collapsingview.R;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<String> listData=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SLog.init(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i=0;i<50;i++){
            listData.add(String.format(Locale.CHINESE,"这是第%d个item",(i+1)));
        }
    }

    public void testCollapsing(View view) {
        startActivity(new Intent(this,TranslucentStatusActivity.class));
    }


    public void testNotCollapsing(View view) {
        startActivity(new Intent(this,NormalActivity.class));
    }


    public void testCollapsingFragment(View view) {
        startActivity(new Intent(this,TranslucentStatusFragmentActivity.class));
    }


    public void testNotCollapsingFragment(View view) {
        startActivity(new Intent(this,NormalFragmentActivity.class));
    }
}
