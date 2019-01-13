package cn.edu.pku.wangtianrun.classroomreservation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    private static final int CAULC_ROOM=2;
    private ViewPager vpager;
    private ArrayList<View> aList;
    private MypagerAdapter mAdapter;
    private LinearLayout mLinearLayout;
    private int mNub=0;
    private ImageView updateBtn;
    private TextView room_3301,room_3302,room_3303,room_3201,room_3202,room_3203,room_3101,room_3102,room_3103,
    room_2301,room_2302,room_2303,room_2201,room_2202,room_2203,room_2101,room_2102,room_2103,weekday;
    private ImageView room_3301_img,room_3302_img,room_3303_img,room_3201_img,room_3202_img,room_3203_img,room_3101_img,room_3102_img,room_3103_img,
            room_2301_img,room_2302_img,room_2303_img,room_2201_img,room_2202_img,room_2203_img,room_2101_img,room_2102_img,room_2103_img;
    private ImageView select_date;
    private myDate dateObj;
    private ImageView userInf;
    private int selectedRoomNub=0;
    private ProgressBar progressBar;

    private Handler mHandler=new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_DATE:
                    updateRoomsInf((myDate) msg.obj);
                    break;
                case CAULC_ROOM:
                    selectedRoomNub=(int)msg.obj;
                    Log.d("userRoomNumber",""+selectedRoomNub);
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
        //为选择日期设置单击事件
        select_date=(ImageView)findViewById(R.id.select_date);
        select_date.setOnClickListener(this);
        //为我的信息设置单击事件
        userInf=(ImageView)findViewById(R.id.mine);
        userInf.setOnClickListener(this);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
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
        room_2101_img=(ImageView)aList.get(1).findViewById(R.id.room_2101_img);
        room_2102=(TextView)aList.get(1).findViewById(R.id.room_2102);
        room_2102_img=(ImageView)aList.get(1).findViewById(R.id.room_2102_img);
        room_2103=(TextView)aList.get(1).findViewById(R.id.room_2103);
        room_2103_img=(ImageView)aList.get(1).findViewById(R.id.room_2103_img);
        room_2201=(TextView)aList.get(1).findViewById(R.id.room_2201);
        room_2201_img=(ImageView)aList.get(1).findViewById(R.id.room_2201_img);
        room_2202=(TextView)aList.get(1).findViewById(R.id.room_2202);
        room_2202_img=(ImageView)aList.get(1).findViewById(R.id.room_2202_img);
        room_2203=(TextView)aList.get(1).findViewById(R.id.room_2203);
        room_2203_img=(ImageView)aList.get(1).findViewById(R.id.room_2203_img);
        room_2301=(TextView)aList.get(1).findViewById(R.id.room_2301);
        room_2301_img=(ImageView)aList.get(1).findViewById(R.id.room_2301_img);
        room_2302=(TextView)aList.get(1).findViewById(R.id.room_2302);
        room_2302_img=(ImageView)aList.get(1).findViewById(R.id.room_2302_img);
        room_2303=(TextView)aList.get(1).findViewById(R.id.room_2303);
        room_2303_img=(ImageView)aList.get(1).findViewById(R.id.room_2303_img);
        room_3101=(TextView)aList.get(0).findViewById(R.id.room_3101);
        room_3101_img=(ImageView)aList.get(0).findViewById(R.id.room_3101_img);
        room_3102=(TextView)aList.get(0).findViewById(R.id.room_3102);
        room_3102_img=(ImageView)aList.get(0).findViewById(R.id.room_3102_img);
        room_3103=(TextView)aList.get(0).findViewById(R.id.room_3103);
        room_3103_img=(ImageView)aList.get(0).findViewById(R.id.room_3103_img);
        room_3201=(TextView)aList.get(0).findViewById(R.id.room_3201);
        room_3201_img=(ImageView)aList.get(0).findViewById(R.id.room_3201_img);
        room_3202=(TextView)aList.get(0).findViewById(R.id.room_3202);
        room_3202_img=(ImageView)aList.get(0).findViewById(R.id.room_3202_img);
        room_3203=(TextView)aList.get(0).findViewById(R.id.room_3203);
        room_3203_img=(ImageView)aList.get(0).findViewById(R.id.room_3203_img);
        room_3301=(TextView)aList.get(0).findViewById(R.id.room_3301);
        room_3301_img=(ImageView)aList.get(0).findViewById(R.id.room_3301_img);
        room_3302=(TextView)aList.get(0).findViewById(R.id.room_3302);
        room_3302_img=(ImageView)aList.get(0).findViewById(R.id.room_3302_img);
        room_3303=(TextView)aList.get(0).findViewById(R.id.room_3303);
        room_3303_img=(ImageView)aList.get(0).findViewById(R.id.room_3303_img);
        weekday=(TextView)findViewById(R.id.date);
        //为房间图标设置单击事件，响应选择教室或会议室的单击事件。
        room_2101_img.setOnClickListener(this);
        room_2102_img.setOnClickListener(this);
        room_2103_img.setOnClickListener(this);
        room_2201_img.setOnClickListener(this);
        room_2202_img.setOnClickListener(this);
        room_2203_img.setOnClickListener(this);
        room_2301_img.setOnClickListener(this);
        room_2302_img.setOnClickListener(this);
        room_2303_img.setOnClickListener(this);
        room_3101_img.setOnClickListener(this);
        room_3102_img.setOnClickListener(this);
        room_3103_img.setOnClickListener(this);
        room_3201_img.setOnClickListener(this);
        room_3202_img.setOnClickListener(this);
        room_3203_img.setOnClickListener(this);
        room_3301_img.setOnClickListener(this);
        room_3302_img.setOnClickListener(this);
        room_3303_img.setOnClickListener(this);
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
    * 获取用户获得房间数目的信息
    * */
    private void queryUserInfo(String username){
        final String address="http://140.143.28.211/rooms/rooms/userinfo/username/"+username;
        Log.d("userInfo",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                try{
                    URL url=new URL(address);
                    con=(HttpURLConnection)url.openConnection();
                    con.setReadTimeout(8000);
                    con.setRequestMethod("GET");
                    InputStream in=con.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String str;
                    while ((str=reader.readLine())!=null){
                        response.append(str);
                        Log.d("userInfo",str);
                    }

                    String responseStr=response.toString();
                    Log.d("userInfo",responseStr);
                    int roomNum=parseJsonInfo(responseStr);
                    Log.d("userInfo","roomList.length is "+roomNum);
                    Message msg=new Message();
                    msg.what=CAULC_ROOM;
                    msg.obj=roomNum;
                    mHandler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
    /*
    * 解析用户预定房间数目
    * */
    private int parseJsonInfo(String jsonData){
        int num=0;
        try {
            JSONArray jsonArray=new JSONArray(jsonData);
            num=jsonArray.length();
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
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
                String date_cn=jsonObject.getString("日期");
                Log.d("selected_date",date_cn);
                dateObj.setDate_cn(date_cn);
                int room_2101=jsonObject.getInt("room_2101");
                Log.d("selected_date",""+room_2101);
                dateObj.setRoom_2101(room_2101);
                int room_2102=jsonObject.getInt("room_2102");
                Log.d("selected_date",""+room_2102);
                dateObj.setRoom_2102(room_2102);
                int room_2103=jsonObject.getInt("room_2103");
                Log.d("selected_date",""+room_2103);
                dateObj.setRoom_2103(room_2103);
                int room_2201=jsonObject.getInt("room_2201");
                Log.d("selected_date",""+room_2201);
                dateObj.setRoom_2201(room_2201);
                int room_2202=jsonObject.getInt("room_2202");
                Log.d("selected_date",""+room_2202);
                dateObj.setRoom_2202(room_2202);
                int room_2203=jsonObject.getInt("room_2203");
                Log.d("selected_date",""+room_2203);
                dateObj.setRoom_2203(room_2203);
                int room_2301=jsonObject.getInt("room_2301");
                Log.d("selected_date",""+room_2301);
                dateObj.setRoom_2301(room_2301);
                int room_2302=jsonObject.getInt("room_2302");
                Log.d("selected_date",""+room_2302);
                dateObj.setRoom_2302(room_2302);
                int room_2303=jsonObject.getInt("room_2303");
                Log.d("selected_date",""+room_2303);
                dateObj.setRoom_2303(room_2303);
                int room_3101=jsonObject.getInt("room_3101");
                Log.d("selected_date",""+room_3101);
                dateObj.setRoom_3101(room_3101);
                int room_3102=jsonObject.getInt("room_3102");
                Log.d("selected_date",""+room_3102);
                dateObj.setRoom_3102(room_3102);
                int room_3103=jsonObject.getInt("room_3103");
                Log.d("selected_date",""+room_3103);
                dateObj.setRoom_3103(room_3103);
                int room_3201=jsonObject.getInt("room_3201");
                Log.d("selected_date",""+room_3201);
                dateObj.setRoom_3201(room_3201);
                int room_3202=jsonObject.getInt("room_3202");
                Log.d("selected_date",""+room_3202);
                dateObj.setRoom_3202(room_3202);
                int room_3203=jsonObject.getInt("room_3203");
                Log.d("selected_date",""+room_3203);
                dateObj.setRoom_3203(room_3203);
                int room_3301=jsonObject.getInt("room_3301");
                Log.d("selected_date",""+room_3301);
                dateObj.setRoom_3301(room_3301);
                int room_3302=jsonObject.getInt("room_3302");
                Log.d("selected_date",""+room_3302);
                dateObj.setRoom_3302(room_3302);
                int room_3303=jsonObject.getInt("room_3303");
                Log.d("selected_date",""+room_3303);
                dateObj.setRoom_3303(room_3303);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateObj;
    }
    /*
    * 更新会议室和教室信息
    * */
    void updateRoomsInf(myDate date){

        weekday.setText(date.getDate_cn());
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
        progressBar.setVisibility(View.INVISIBLE);
        updateBtn.setVisibility(View.VISIBLE);
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1&&resultCode==RESULT_OK){
            String date=data.getStringExtra("date");
            Log.d("selected_date",date);
            //将选择的日期通过sharedpreference保存
            SharedPreferences.Editor editor=getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putString("date",date);
            editor.apply();

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                //获取网络数据
                queryRoomInf(date);
                Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("selected_date","click");
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String date=sharedPreferences.getString("date","monday");
        Log.d("selected_date",date);
        //更新事件
        if(v.getId()==R.id.update){
            progressBar.setVisibility(View.VISIBLE);
            updateBtn.setVisibility(View.INVISIBLE);
            Log.d("selected_date",date);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                //获取网络数据
                queryRoomInf(date);
                Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
            }
        }
        //选择日期事件
        else if(v.getId()==R.id.select_date){
            Intent i=new Intent(this,selectDate.class);
            i.putExtra("initDate",date);
            startActivityForResult(i,1);
        }
        //查看我的信息事件
        else if(v.getId()==R.id.mine){
            Intent intent=new Intent(this,userInf.class);
            startActivity(intent);
        }
        //选择教室事件
        else if(v.getId()==R.id.room_2101_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_2101(),2101);
        }
        else if(v.getId()==R.id.room_2102_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_2102(),2102);
        }
        else if(v.getId()==R.id.room_2103_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_2103(),2103);
        }
        else if(v.getId()==R.id.room_2201_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_2201(),2201);
        }
        else if(v.getId()==R.id.room_2202_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_2202(),2202);
        }
        else if(v.getId()==R.id.room_2203_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_2203(),2203);
        }
        else if(v.getId()==R.id.room_2301_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_2301(),2301);
        }
        else if(v.getId()==R.id.room_2302_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_2302(),2302);
        }
        else if(v.getId()==R.id.room_2303_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_2303(),2303);
        }

        else if(v.getId()==R.id.room_3101_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_3101(),3101);
        }
        else if(v.getId()==R.id.room_3102_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_3102(),3102);
        }
        else if(v.getId()==R.id.room_3103_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_3103(),3103);
        }
        else if(v.getId()==R.id.room_3201_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_3201(),3201);
        }
        else if(v.getId()==R.id.room_3202_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_3202(),3202);
        }
        else if(v.getId()==R.id.room_3203_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_3203(),3203);
        }
        else if(v.getId()==R.id.room_3301_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_3301(),3301);
        }
        else if(v.getId()==R.id.room_3302_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_3302(),3302);
        }
        else if(v.getId()==R.id.room_3303_img){
            queryRoomInf(date);
            responseChooseRoom(dateObj.getRoom_3303(),3303);
        }
    }
    /*
    * 处理选择教室或会议室事件的方法
    * */
    private void responseChooseRoom(int type,int room){
        if(type==0){
            queryUserInfo("wang");
            Toast toast=Toast.makeText(getApplicationContext(),"房间已有课，请另行选择",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        else if(type==1){
            queryUserInfo("wang");
            if(selectedRoomNub>=3){
                Toast toast=Toast.makeText(getApplicationContext(),"预定数目已达上限，预定失败",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }else {
                setRoomInf(dateObj.getDate(),room);
                Toast toast=Toast.makeText(getApplicationContext(),"预定成功",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                queryRoomInf(dateObj.getDate());
            }
        }
        else if(type==2){
            queryUserInfo("wang");
            Toast toast=Toast.makeText(getApplicationContext(),"房间已被预定",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }
    /*
    * 选择教室或会议室后向服务器发送更新数据库的信息
    * */
    private void setRoomInf(String date,int room){
        final String address="http://140.143.28.211/rooms/rooms/write/date/"+date+"/room/room_"+room+"/username/wang";
        Log.d("write_date",address);
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
                        Log.d("write_date",str);
                    }
                    String responseStr=response.toString();
                    Log.d("write_date",responseStr);
//                    if(responseStr!=null){
//                        view.setText("");
//                    }
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

}
