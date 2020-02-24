# AreaSetView
  > 一个选取网格范围的控件，效果如下

  ![GIF](https://github.com/looogen/AreaSetView/blob/master/gif/GIF.gif)

  **使用方式** 

> 直接复制到项目的目录下使用

- 设置布局，该控件本身透明可以覆盖在上层使用。

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <android.support.v7.widget.Toolbar
        android:id="@+id/tool_bar"
        android:layout_width="match_parent"
        android:layout_height="?actionBarSize" />
    <ImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
       android:background="@drawable/android_25"/>
    <com.llg.areasetview.AreaSetView
        android:id="@+id/area_set_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_centerInParent="true"/>
</RelativeLayout>
  
  ```

- 初始化设置数据

```java
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
```
