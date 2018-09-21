package com.llg.areasetview;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private int col = 30;
    private int row = 20;
    private int total = col * row;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 根据选中的格子 生成数组
     *
     * @param points
     * @return
     */
    private int[] culBlockRegion(ArrayList<Point> points) {
        int length = (int) Math.ceil(total / 8);
        int[] array = new int[length];

        int patch = (length * 8) - total;

        StringBuilder sb = new StringBuilder();

        for (int i = 0, k = 0, count = 0; i < row; i++) {

            for (int j = 0; j < col; j++, count++) {

                Point point = new Point(j, i);
                if (points.contains(point)) {
                    sb.append(1);
                } else {
                    sb.append(0);
                }

                if ((count + 1) % 8 == 0) {
                    array[k] = Integer.parseInt(sb.toString(), 2);
                    k++;
                    sb.delete(0, sb.length());
                }

                //跳出循环时，补够8bit
                if (count == total) {
                    for (int p = 0; p < patch; p++) {
                        sb.append(0);
                    }
                    array[k] = Integer.parseInt(sb.toString(), 2);
                }
            }
        }
        return array;
    }

    /**
     * 计算选中的格子的坐标
     *
     * @param
     * @return
     */
    private ArrayList<Point> culSelectPoints(int[] block) {
        ArrayList<Point> points = new ArrayList<>();


        for (int i = 0, count = 0; i < block.length; i++) {
            String binaryString = toBinary(block[i],8);
            Log.e("binary",binaryString);
            for (int j = 0; j < binaryString.length() && count <= total; j++, count++) {
                if (binaryString.charAt(j) == '1') {
                    Log.e("count2",count+"");
                    int x = count % col;
                    int y = count / col;
                    Point point = new Point(x, y);
                    points.add(point);
                }
            }
        }
        return points;
    }

    /**
     * 将一个int数字转换为二进制的字符串形式。
     * @param num 需要转换的int类型数据
     * @param digits 要转换的二进制位数，位数不足则在前面补0
     * @return 二进制的字符串形式
     */
    public static String toBinary(int num, int digits) {
        String cover = Integer.toBinaryString(1 << digits).substring(1);
        String s = Integer.toBinaryString(num);
        return s.length() < digits ? cover.substring(s.length()) + s : s;
    }
}
