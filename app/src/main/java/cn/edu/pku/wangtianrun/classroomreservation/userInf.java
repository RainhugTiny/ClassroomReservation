package cn.edu.pku.wangtianrun.classroomreservation;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class userInf extends Activity implements View.OnClickListener {
    private ImageView mainPage;
    private ImageView updateBtn;
    private ListView mListView;
    private static  final int UPDATE_LIST=1;

    private Handler mHandler=new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_LIST:
                    initListView((String[]) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        //为返回房间列表设置单击事件
        mainPage=(ImageView)findViewById(R.id.back_main_page);
        mainPage.setOnClickListener(this);
        mListView=(ListView)findViewById(R.id.personalListView);
        //为获取用户预定房间信息设置单击事件
        updateBtn=(ImageView)findViewById(R.id.info_update);
        updateBtn.setOnClickListener(this);
    }
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
                    String[] roomList=parseJsonInfo(responseStr);
                    Log.d("userInfo","roomList.length is"+roomList.length);
                    Message msg=new Message();
                    msg.what=UPDATE_LIST;
                    msg.obj=roomList;
                    mHandler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private String[] parseJsonInfo(String jsonData){
        ArrayList<String> mineRooms=new ArrayList<>();
        int num=0;
        String[] roomList;
        try {
            JSONArray jsonArray=new JSONArray(jsonData);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String room=jsonObject.getString("room");
                String date=jsonObject.getString("date");
                if(date.equals("monday")){
                    date="星期一";
                }else if(date.equals("tuesday")){
                    date="星期二";
                }else if(date.equals("wednesday")){
                    date="星期三";
                }else if(date.equals("thursday")){
                    date="星期四";
                }else if(date.equals("friday")){
                    date="星期五";
                }else if(date.equals("saturday")){
                    date="星期六";
                }else if(date.equals("sunday")){
                    date="星期日";
                }
                String info=date+"    教室:"+room.charAt(5)+room.charAt(6)+room.charAt(7)+room.charAt(8);
                mineRooms.add(info);
                num++;
                Log.d("userInfo",mineRooms.get(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        roomList=new String[num];
        for (int i=0;i<num;i++){
            roomList[i]=mineRooms.get(i);
            Log.d("userInfo","roomlist "+i+" is "+roomList[i]);
        }
        return roomList;
    }
    private void initListView(String[] mineRooms){
        ArrayAdapter<String> adapter=new ArrayAdapter<>(userInf.this,android.R.layout.simple_list_item_1,mineRooms);
        mListView.setAdapter(adapter);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(userInf.this,"你选择了："+position,Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.back_main_page){
            finish();
        }
        if(v.getId()==R.id.info_update){
            queryUserInfo("wang");
        }
    }

}
