package cn.edu.pku.wangtianrun.classroomreservation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import cn.edu.pku.wangtianrun.classroomreservation.viewPager.MypagerAdapter;

public class guidePage extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private ArrayList<View> aList;
    private MypagerAdapter mypagerAdapter;
    private Button registButton;
    private Button loginButton;
    private LinearLayout mLinearLayout;
    int now=0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_main);
        //小圆点所在的线性布局
        mLinearLayout=(LinearLayout)findViewById(R.id.guide_linear);
        //初始化界面
        init();
        mLinearLayout.getChildAt(0).setEnabled(true);
        viewPager.addOnPageChangeListener(this);
        registButton=(Button)aList.get(1).findViewById(R.id.regist_btn);
        //为注册按钮设置监听点击事件
        registButton.setOnClickListener(this);
        loginButton=(Button)aList.get(1).findViewById(R.id.login_btn);
        //为登陆按钮设置监听点击事件
        loginButton.setOnClickListener(this);
    }
    private void init(){
        viewPager=(ViewPager)findViewById(R.id.guide);
        aList=new ArrayList<View>();
        LayoutInflater Li=getLayoutInflater();
        //储存两个页面布局
        aList.add(Li.inflate(R.layout.guide_page_one,null,false));
        aList.add(Li.inflate(R.layout.guide_page_two,null,false));
        mypagerAdapter=new MypagerAdapter(aList);
        viewPager.setAdapter(mypagerAdapter);

        View view;
        //为页面设置小圆点
        for(int i=0;i<2;i++){
            view=new View(guidePage.this);
            //background为自定义的小圆点
            view.setBackgroundResource(R.drawable.background);
            view.setEnabled(false);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(30,30);
            //为不同页面的小圆点设置间距
            if(i!=0){
                layoutParams.leftMargin=10;
            }
            mLinearLayout.addView(view,layoutParams);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.regist_btn){
            Toast.makeText(guidePage.this,"注册成功",Toast.LENGTH_SHORT).show();
        }
        if(v.getId()==R.id.login_btn){
            loginButton.setBackgroundColor(Color.parseColor("#F5F5DC"));
            //通过Intent启动MainActivity活动
            Intent intent=new Intent(guidePage.this,MainActivity.class);
            startActivity(intent);
            //此活动结束
            finish();
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        //刚才选中页面的小圆点设置为未选中样式
        mLinearLayout.getChildAt(now).setEnabled(false);
        //当前选中页面的小圆点设置为选中样式
        mLinearLayout.getChildAt(position).setEnabled(true);
        now=position;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
