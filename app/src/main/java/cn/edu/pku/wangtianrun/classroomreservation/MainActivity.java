package cn.edu.pku.wangtianrun.classroomreservation;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cn.edu.pku.wangtianrun.classroomreservation.util.NetUtil;
import cn.edu.pku.wangtianrun.classroomreservation.viewPager.MypagerAdapter;

public class MainActivity extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager vpager;
    private ArrayList<View> aList;
    private MypagerAdapter mAdapter;
    private LinearLayout mLinearLayout;
    private int mNub=0;
    private ImageView updateBtn;
    private ImageView room_3301,room_3302,room_3303,room_3201,room_3202,room_3203,room_3101,room_3102,room_3103,
    room_2301,room_2302,room_2303,room_2201,room_2202,room_2203,room_2101,room_2102,room_2103;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_info);
        updateBtn=(ImageView)findViewById(R.id.update);
        updateBtn.setOnClickListener(this);
        //初始化滑动界面
        initViewPager();
        mLinearLayout=(LinearLayout)findViewById(R.id.main_linear);
        //初始化展示未来四天天气界面的小圆点
        initDot();
        //为滑动界面设置页面滑动监听事件
        vpager.addOnPageChangeListener(this);
        //设置当前页面的小圆点可见
        mLinearLayout.getChildAt(0).setEnabled(true);

        /*进行网络状态检测。
         *通过Toast在界面通知信息。
         */
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
        }

    }
    private void initViewPager(){
        vpager=(ViewPager)findViewById(R.id.vpager);
        aList=new ArrayList<View>();
        //使用LayoutInflater来载入需要动态载入的界面
        LayoutInflater Li=getLayoutInflater();
        //在aList中添加布局
        aList.add(Li.inflate(R.layout.view_one,null,false));
        aList.add(Li.inflate(R.layout.view_two,null,false));
        mAdapter=new MypagerAdapter(aList);
        vpager.setAdapter(mAdapter);

        //初始化房间信息
        
    }
    /*
     * 构造初始化小圆点方法
     * */
    private void initDot(){
        View view;
        for(int i=0;i<2;i++){
            view=new View(MainActivity.this);
            //加载自定义的布局
            view.setBackgroundResource(R.drawable.background);
            //设置小圆点为未选中样式
            view.setEnabled(false);
            //设置布局参数
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(30,30);
            if(i!=0){
                //设置小圆点的间距
                layoutParams.leftMargin=10;
            }
            //在布局中添加元素
            mLinearLayout.addView(view,layoutParams);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        //过去选中的页面小圆点不可见
        mLinearLayout.getChildAt(mNub).setEnabled(false);
        //当前选中页面的小圆点可见
        mLinearLayout.getChildAt(i).setEnabled(true);
        //记录当前位置
        mNub=i;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
    private void queryRoomInf(String date){
        final String address="http://140.143.28.211/rooms/rooms/read/date/"+date;
        Log.d("selected_date",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                try {
                    URL url=new URL(address);
                    con=(HttpURLConnection)url.openConnection();
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in=con.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String str;
                    while ((str=reader.readLine())!=null){
                        response.append(str);
                        Log.d("selected_date",str);
                    }
                    String responseStr=response.toString();
                    Log.d("selected_date",responseStr);
                    parseJson(responseStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(con!=null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }
    private void parseJson(String jsonData){
        try {
            JSONArray jsonArray=new JSONArray(jsonData);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);

                String date=jsonObject.getString("date");
                Log.d("selected_date",date);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.update){
            SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
            String date=sharedPreferences.getString("date","monday");
            Log.d("selected_date",date);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                //获取网络数据
                queryRoomInf(date);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
