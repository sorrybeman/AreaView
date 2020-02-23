package com.llg.areasetview;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    private AreaSetView mAreaSetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAreaSetView = findViewById(R.id.area_set_view);
        //设置网格的x,y方向的数目
        mAreaSetView.setXYCount(30,20);

        //模拟选中的数据
        HashSet<Point> selectedArea = new HashSet<>();
        selectedArea.add(new Point(20,30));
        selectedArea.add(new Point(21,30));
        selectedArea.add(new Point(22,30));
        selectedArea.add(new Point(23,30));
        selectedArea.add(new Point(24,30));
        selectedArea.add(new Point(25,30));
        selectedArea.add(new Point(26,30));
        selectedArea.add(new Point(27,31));
        selectedArea.add(new Point(20,31));
        selectedArea.add(new Point(21,31));
        selectedArea.add(new Point(22,31));
        selectedArea.add(new Point(23,32));
        selectedArea.add(new Point(24,32));
        selectedArea.add(new Point(25,32));
        selectedArea.add(new Point(26,33));
        selectedArea.add(new Point(27,33));

        //设置选中区域
        mAreaSetView.setSelectArea(selectedArea);
    }
}
