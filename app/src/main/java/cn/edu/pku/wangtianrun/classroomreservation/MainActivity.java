package cn.edu.pku.wangtianrun.classroomreservation;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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


import cn.edu.pku.wangtianrun.classroomreservation.bean.myDate;
import cn.edu.pku.wangtianrun.classroomreservation.util.NetUtil;
import cn.edu.pku.wangtianrun.classroomreservation.viewPager.MypagerAdapter;

public class MainActivity extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final int UPDATE_DATE=1;
    private ViewPager vpager;
    private ArrayList<View> aList;
    private MypagerAdapter mAdapter;
    private LinearLayout mLinearLayout;
    private int mNub=0;
    private ImageView updateBtn;
    private TextView room_3301,room_3302,room_3303,room_3201,room_3202,room_3203,room_3101,room_3102,room_3103,
    room_2301,room_2302,room_2303,room_2201,room_2202,room_2203,room_2101,room_2102,room_2103,weekday;

    private Handler mHandler=new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_DATE:
                    updateRoomsInf((myDate) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

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
        room_2101=(TextView)aList.get(1).findViewById(R.id.room_2101);
        room_2102=(TextView)aList.get(1).findViewById(R.id.room_2102);
        room_2103=(TextView)aList.get(1).findViewById(R.id.room_2103);
        room_2201=(TextView)aList.get(1).findViewById(R.id.room_2201);
        room_2202=(TextView)aList.get(1).findViewById(R.id.room_2202);
        room_2203=(TextView)aList.get(1).findViewById(R.id.room_2203);
        room_2301=(TextView)aList.get(1).findViewById(R.id.room_2301);
        room_2302=(TextView)aList.get(1).findViewById(R.id.room_2302);
        room_2303=(TextView)aList.get(1).findViewById(R.id.room_2303);
        room_3101=(TextView)aList.get(0).findViewById(R.id.room_3101);
        room_3102=(TextView)aList.get(0).findViewById(R.id.room_3102);
        room_3103=(TextView)aList.get(0).findViewById(R.id.room_3103);
        room_3201=(TextView)aList.get(0).findViewById(R.id.room_3201);
        room_3202=(TextView)aList.get(0).findViewById(R.id.room_3202);
        room_3203=(TextView)aList.get(0).findViewById(R.id.room_3203);
        room_3301=(TextView)aList.get(0).findViewById(R.id.room_3301);
        room_3302=(TextView)aList.get(0).findViewById(R.id.room_3302);
        room_3303=(TextView)aList.get(0).findViewById(R.id.room_3303);
        weekday=(TextView)findViewById(R.id.date);
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
    /*
    * 获取网络数据
    * */
    private void queryRoomInf(final String date){
        final String address="http://140.143.28.211/rooms/rooms/read/date/"+date;
        Log.d("selected_date",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                myDate dateObj=null;
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
                    dateObj=parseJson(responseStr);
                    if(dateObj!=null){
                        Log.d("selected_date",dateObj.toString());
                        Message msg=new Message();
                        msg.what=UPDATE_DATE;
                        msg.obj=dateObj;
                        mHandler.sendMessage(msg);
                    }
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
    /*
    * 解析Json数据
    * */
    private myDate parseJson(String jsonData){
        myDate dateObj=new myDate();
        try {
            JSONArray jsonArray=new JSONArray(jsonData);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);

                String date=jsonObject.getString("date");
                Log.d("selected_date",date);
                dateObj.setDate(date);
                int room_2101=jsonObject.getInt("2101");
                Log.d("selected_date",""+room_2101);
                dateObj.setRoom_2101(room_2101);
                int room_2102=jsonObject.getInt("2102");
                Log.d("selected_date",""+room_2102);
                dateObj.setRoom_2102(room_2102);
                int room_2103=jsonObject.getInt("2103");
                Log.d("selected_date",""+room_2103);
                dateObj.setRoom_2103(room_2103);
                int room_2201=jsonObject.getInt("2201");
                Log.d("selected_date",""+room_2201);
                dateObj.setRoom_2201(room_2201);
                int room_2202=jsonObject.getInt("2202");
                Log.d("selected_date",""+room_2202);
                dateObj.setRoom_2202(room_2202);
                int room_2203=jsonObject.getInt("2203");
                Log.d("selected_date",""+room_2203);
                dateObj.setRoom_2203(room_2203);
                int room_2301=jsonObject.getInt("2301");
                Log.d("selected_date",""+room_2301);
                dateObj.setRoom_2301(room_2301);
                int room_2302=jsonObject.getInt("2302");
                Log.d("selected_date",""+room_2302);
                dateObj.setRoom_2302(room_2302);
                int room_2303=jsonObject.getInt("2303");
                Log.d("selected_date",""+room_2303);
                dateObj.setRoom_2303(room_2303);
                int room_3101=jsonObject.getInt("3101");
                Log.d("selected_date",""+room_3101);
                dateObj.setRoom_3101(room_3101);
                int room_3102=jsonObject.getInt("3102");
                Log.d("selected_date",""+room_3102);
                dateObj.setRoom_3102(room_3102);
                int room_3103=jsonObject.getInt("3103");
                Log.d("selected_date",""+room_3103);
                dateObj.setRoom_3103(room_3103);
                int room_3201=jsonObject.getInt("3201");
                Log.d("selected_date",""+room_3201);
                dateObj.setRoom_3201(room_3201);
                int room_3202=jsonObject.getInt("3202");
                Log.d("selected_date",""+room_3202);
                dateObj.setRoom_3202(room_3202);
                int room_3203=jsonObject.getInt("3203");
                Log.d("selected_date",""+room_3203);
                dateObj.setRoom_3203(room_3203);
                int room_3301=jsonObject.getInt("3301");
                Log.d("selected_date",""+room_3301);
                dateObj.setRoom_3301(room_3301);
                int room_3302=jsonObject.getInt("3302");
                Log.d("selected_date",""+room_3302);
                dateObj.setRoom_3302(room_3302);
                int room_3303=jsonObject.getInt("3303");
                Log.d("selected_date",""+room_3303);
                dateObj.setRoom_3303(room_3303);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateObj;
    }
    void updateRoomsInf(myDate date){

        weekday.setText(date.getDate());
        int[] stateList=date.stateList();
        String[] roomNames={"2101","2102","2103","2201","2202","2203","2301","2302","2303",
                "3101","3102","3103","3201","3202","3203","3301","3302","3303",};
        ArrayList<TextView> arrayList=new ArrayList<>();
        arrayList.add(room_2101);
        arrayList.add(room_2102);
        arrayList.add(room_2103);
        arrayList.add(room_2201);
        arrayList.add(room_2202);
        arrayList.add(room_2203);
        arrayList.add(room_2301);
        arrayList.add(room_2302);
        arrayList.add(room_2303);
        arrayList.add(room_3101);
        arrayList.add(room_3102);
        arrayList.add(room_3103);
        arrayList.add(room_3201);
        arrayList.add(room_3202);
        arrayList.add(room_3203);
        arrayList.add(room_3301);
        arrayList.add(room_3302);
        arrayList.add(room_3303);
        int state;
        for(int i=0;i<18;i++){
            //用数字代表教室的状态，0代表有课，1代表空闲，2代表已预约
            TextView view=arrayList.get(i);
            if(stateList[i]==0){
                view.setText(roomNames[i]+"   有课");
            }else if(stateList[i]==1){
                view.setText(roomNames[i]+"   空闲");
            }else if(stateList[i]==2){
                view.setText(roomNames[i]+"  已预定");
            }
        }
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
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
