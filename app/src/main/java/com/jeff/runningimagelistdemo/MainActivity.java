package com.jeff.runningimagelistdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ScrollingImageListView mScrollingImageListView;

    //button
    private Button btn1;
    private Button btn2;
    private Button btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        mScrollingImageListView = (ScrollingImageListView) findViewById(R.id.test);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.ic_launcher);
        list.add(R.mipmap.a1);
        list.add(R.mipmap.a2);
        list.add(R.mipmap.a3);
        //设置轮播图片列表
        mScrollingImageListView.setImgSrcList(list);
        //设置方向是否从左至右
        mScrollingImageListView.setLeftToRight(true);
        //设置图片是否乱序
        mScrollingImageListView.setOutOfOder(false);
        //设置播放速度
        mScrollingImageListView.setSpeed(5);
        //播放开始
        mScrollingImageListView.startRun();

    }

    @Override
    public void onClick(View v) {
        switch (mScrollingImageListView.getmRunningmState()) {
            case STOP:
                btn2.setEnabled(true);
                break;
            case LEFT_RUNNING:
                btn1.setEnabled(true);
                break;
            case RIGHT_RUNNING:
                btn3.setEnabled(true);
                break;
        }
        switch (v.getId()) {
            case R.id.btn1:
                btn1.setEnabled(false);
                mScrollingImageListView.changeDirection(ScrollingImageListView.State.LEFT_RUNNING);
                break;
            case R.id.btn2:
                btn2.setEnabled(false);
                mScrollingImageListView.changeDirection(ScrollingImageListView.State.STOP);
                break;
            case R.id.btn3:
                btn3.setEnabled(false);
                mScrollingImageListView.changeDirection(ScrollingImageListView.State.RIGHT_RUNNING);
                break;
        }
    }
}