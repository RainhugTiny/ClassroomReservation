package cn.edu.pku.wangtianrun.classroomreservation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class selectDate extends Activity implements View.OnClickListener {
    private ListView listView;
    private ImageView backBtn;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.select_date);
        initListView();
        backBtn=(ImageView)findViewById(R.id.back);
        backBtn.setOnClickListener(this);
    }
    /*
    * 初试化选择天气列表
    * */
    private void initListView(){
        final String[] date_cn={"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
        final String[] date={"monday","tuesday","wednesday","thursday","friday","saturday","sunday"};
        listView=(ListView)findViewById(R.id.myListView);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(selectDate.this,android.R.layout.simple_list_item_1,date_cn);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDate=date[position];
                Toast.makeText(selectDate.this,"你选择了："+date_cn[position],Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.back){
            Intent i=new Intent();
            i.putExtra("date",selectedDate);
            setResult(RESULT_OK,i);
            finish();
        }
    }
}
