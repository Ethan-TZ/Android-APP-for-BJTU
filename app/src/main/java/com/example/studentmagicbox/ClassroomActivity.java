package com.example.studentmagicbox;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClassroomActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView backto;
    Button syb, syxb, sydb, jiujb, bajb, yfb, jxb, d1b;
    Button c1, c2, c3, c4, c5, c6, c7;
    Button  class_stays, time_stays; //纪录当前状态条状态
    Calendar calendar;
    private int hour ;
    private int current_classes;
    private String class_key;
    private int time_key;
    private static  int alpha = 40;

    private void initview()
    {
        backto=findViewById(R.id.backto);

        syb = findViewById(R.id.classroom_syb);
        syxb = findViewById(R.id.classroom_syxb);
        sydb = findViewById(R.id.classroom_sydb);
        jiujb = findViewById(R.id.classroom_jiuj);
        bajb = findViewById(R.id.classroom_baj);
        yfb = findViewById(R.id.classroom_yf);
        jxb = findViewById(R.id.classroom_jx);
        d1b = findViewById(R.id.classroom_d1);
        syb.getBackground().setAlpha(alpha);
        syxb.getBackground().setAlpha(alpha);
        sydb.getBackground().setAlpha(alpha);
        jiujb.getBackground().setAlpha(alpha);
        bajb.getBackground().setAlpha(alpha);
        yfb.getBackground().setAlpha(alpha);
        jxb.getBackground().setAlpha(alpha);
        d1b.getBackground().setAlpha(alpha);

        c1 = findViewById(R.id.time_c1);
        c2 = findViewById(R.id.time_c2);
        c3 = findViewById(R.id.time_c3);
        c4 = findViewById(R.id.time_c4);
        c5 = findViewById(R.id.time_c5);
        c6 = findViewById(R.id.time_c6);
        c7 = findViewById(R.id.time_c7);
        c1.getBackground().setAlpha(alpha);
        c2.getBackground().setAlpha(alpha);
        c3.getBackground().setAlpha(alpha);
        c4.getBackground().setAlpha(alpha);
        c5.getBackground().setAlpha(alpha);
        c6.getBackground().setAlpha(alpha);
        c7.getBackground().setAlpha(alpha);


        syb.setOnClickListener(this);
        syxb.setOnClickListener(this);
        sydb.setOnClickListener(this);
        jiujb.setOnClickListener(this);
        bajb.setOnClickListener(this);
        yfb.setOnClickListener(this);
        jxb.setOnClickListener(this);
        d1b.setOnClickListener(this);

        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        c5.setOnClickListener(this);
        c6.setOnClickListener(this);
        c7.setOnClickListener(this);
        class_stays = null;
        time_stays = null;
    }
    private void set_currentClasses()
    {
        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour == 8 || hour == 9){
            current_classes = 0;
        }
        else if(hour == 10 || hour == 11){
            current_classes = 1;
        }
        else if(hour == 12 || hour == 13){
            current_classes = 2;
        }
        else if(hour == 14 || hour == 15){
            current_classes = 3;
        }
        else if(hour == 16 || hour == 17){
            current_classes = 4;
        }
        else if(hour == 18){
            current_classes = 100;
        }
        else if(hour == 19|| hour ==20){
            current_classes = 5;
        }
        else if(hour == 21){
            current_classes = 6;
        }
        else {
            current_classes = 100;
        }
        time_key = current_classes;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        set_currentClasses();
        class_key = "";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        initview();
        add_classroom();

        backto.setOnClickListener(e->{
            startActivity(new Intent(ClassroomActivity.this,MainContet.class));
            finish(); });

    }

    private void refresh_class_staybox(Button btn)
    {
        if(class_stays == null){
            class_stays = btn;
            System.out.println("null");
            btn.getBackground().setAlpha((120));
        }
        else if(btn == class_stays){
            class_stays = null;
            btn.getBackground().setAlpha(alpha);
        }
        else {
            class_stays.getBackground().setAlpha(alpha);
            btn.getBackground().setAlpha(120);
            class_stays = btn;
        }
    }

    private void refresh_time_staybox(Button btn)
    {
        if(time_stays == null){
            time_stays = btn;
            btn.getBackground().setAlpha((120));
        }
        else if(btn == time_stays){
            time_stays = null;
            btn.getBackground().setAlpha(alpha);
        }
        else {
            time_stays.getBackground().setAlpha(alpha);
            btn.getBackground().setAlpha(120);
            time_stays = btn;
        }
    }

    private void refresh_class_key(String key){
        if(class_key == "")
            class_key = key;
        else if(class_key == key)
            class_key = "";
        else
            class_key = key;
        System.out.println(class_key);

    }

    private void refresh_timekey(int time){
        if(time_key == time)
            time_key = current_classes;
        else
            time_key = time;
        System.out.println(time_key);
    }

    private void delete_classroom() {
        LinearLayout left_Content = findViewById(R.id.classroom_list_left);
        LinearLayout right_Content = findViewById(R.id.classroom_list_right);
        left_Content.removeAllViews();
        right_Content.removeAllViews();
    }

    private void add_classroom() {
        if (time_key != 100) {
            List<String> roomList = new ArrayList<>(MainActivity.classroomBean.RoomList[time_key]);
            List<String> output = new ArrayList<>();
            if (!class_key.equals("")) {
                if (!class_key.equals("9"))
                    for (int i = 0; i < roomList.size(); i++) {
                        if (roomList.get(i).indexOf(class_key) == 0) {
                            for (; i < roomList.size() && roomList.get(i).indexOf(class_key) == 0; i++)
                                output.add(roomList.get(i));
                            break;
                        }
                    }
                else
                    for (int i = 0; i < roomList.size(); i++) {
                        if ((roomList.get(i).indexOf("东") == 0 || roomList.get(i).indexOf("中") == 0) && roomList.get(i).indexOf("东区") == -1) {
                            for (; i < roomList.size() && ((roomList.get(i).indexOf("东") == 0 || roomList.get(i).indexOf("中") == 0) && roomList.get(i).indexOf("东区") == -1); i++)
                                output.add(roomList.get(i));
                            break;
                        }
                    }
            }


            LinearLayout left_Content = findViewById(R.id.classroom_list_left);
            LinearLayout right_Content = findViewById(R.id.classroom_list_right);
            if (class_key != "")
                for (int i = 0; i < output.size(); i++) {
                    TextView textView = new TextView(this);
                    textView.setText(output.get(i));
                    textView.setTextSize(17);
                    textView.setTextColor(Color.WHITE);
                    textView.setGravity(Gravity.LEFT);
                    left_Content.addView(textView);

                    TextView textView_r = new TextView(this);
                    textView_r.setText(" 空闲");
                    textView_r.setTextSize(17);
                    textView_r.setTextColor(Color.WHITE);
                    textView_r.setGravity(Gravity.RIGHT);
                    right_Content.addView(textView_r);
                }
            else
                for (int i = 0; i < MainActivity.classroomBean.RoomList[time_key].size(); i++) {
                    TextView textView = new TextView(this);
                    textView.setText(MainActivity.classroomBean.RoomList[time_key].get(i));
                    textView.setTextSize(17);
                    textView.setTextColor(Color.WHITE);
                    textView.setGravity(Gravity.LEFT);
                    left_Content.addView(textView);

                    TextView textView_r = new TextView(this);
                    textView_r.setText(" 空闲");
                    textView_r.setTextSize(17);
                    textView_r.setTextColor(Color.WHITE);
                    textView_r.setGravity(Gravity.RIGHT);
                    right_Content.addView(textView_r);
                }
        }
    }




    @Override
    public void onClick(View btn) {
        switch (btn.getId())
        {
            case R.id.classroom_syb:
                refresh_class_staybox(syb);
                refresh_class_key("SY");
                break;
            case R.id.classroom_syxb:
                refresh_class_staybox(syxb);
                refresh_class_key("SX");
                break;
            case R.id.classroom_sydb:
                refresh_class_staybox(sydb);
                refresh_class_key("SD");
                break;
            case R.id.classroom_jiuj:
                refresh_class_staybox(jiujb);
                refresh_class_key("9");
                break;
            case R.id.classroom_baj:
                refresh_class_staybox(bajb);
                refresh_class_key("8");
                break;
            case R.id.classroom_yf:
                refresh_class_staybox(yfb);
                refresh_class_key("YF");
                break;
            case R.id.classroom_jx:
                refresh_class_staybox(jxb);
                refresh_class_key("Z");
                break;
            case R.id.classroom_d1:
                refresh_class_staybox(d1b);
                refresh_class_key("DQ");
                break;
            case R.id.time_c1:
                refresh_time_staybox(c1);
                refresh_timekey(0);
                break;
            case R.id.time_c2:
                refresh_time_staybox(c2);
                refresh_timekey(1);
                break;
            case R.id.time_c3:
                refresh_time_staybox(c3);
                refresh_timekey(2);
                break;
            case R.id.time_c4:
                refresh_time_staybox(c4);
                refresh_timekey(3);
                break;
            case R.id.time_c5:
                refresh_time_staybox(c5);
                refresh_timekey(4);
                break;
            case R.id.time_c6:
                refresh_time_staybox(c6);
                refresh_timekey(5);
                break;
            case R.id.time_c7:
                refresh_time_staybox(c7);
                refresh_timekey(6);
                break;
            default:
                ;
        }
        delete_classroom();
        add_classroom();

    }
}


