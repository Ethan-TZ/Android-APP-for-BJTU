package com.example.studentmagicbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class TimeTableActivity extends AppCompatActivity {
    private ImageView backto;
    private LinearLayout ll1,ll2,ll3,ll4,ll5,ll6,ll7;
    private TextView title, top;
    private String[][] schedule;
    private int week;
    private Calendar calendar;
    private void initview()
    {

        ll1 = findViewById(R.id.column_1);
        ll2 = findViewById(R.id.column_2);
        ll3 = findViewById(R.id.column_3);
        ll4 = findViewById(R.id.column_4);
        ll5 = findViewById(R.id.column_5);
        ll6 = findViewById(R.id.column_6);
        ll7 = findViewById(R.id.column_7);

        title = findViewById(R.id.righttop);
        title.setText("第" + Integer.toString(week) + "周");
        string_handle();
        calendar = Calendar.getInstance();

        top = findViewById(R.id.month);
        top.setText(Integer.toString(calendar.get(Calendar.MONTH) + 1) + "/" +Integer.toString(calendar.get(Calendar.DATE)));

        setday(schedule[0], ll1);
        setday(schedule[1], ll2);
        setday(schedule[2], ll3);
        setday(schedule[3], ll4);
        setday(schedule[4], ll5);
        setday(schedule[5], ll6);
        setday(schedule[6], ll7);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        week = MainActivity.emailBean.jiaoxuezhou;
        initview();
        backto=findViewById(R.id.backto);
        backto.setOnClickListener(e->{
            startActivity(new Intent(TimeTableActivity.this,MainContet.class));
            finish(); });


    }

    private void string_handle(){
        System.out.println("week:" + week);
        schedule = new String[7][];
        for(int i = 0; i < 7; i++){
            schedule[i] = new String[7];
            for(int j = 0; j < 7; j++){
                schedule[i][j] = "";
                if(!MainActivity.scheduleBean.schedule[i][j].equals("")) {
                    System.out.println("table:"+MainActivity.scheduleBean.schedule[i][j]);
                    MainActivity.scheduleBean.schedule[i][j] = MainActivity.scheduleBean.schedule[i][j].trim();
                    String[] str = MainActivity.scheduleBean.schedule[i][j].split(" +");
                    int count = str.length/5;
                    for(int k = 0; k < count; k++){
                        if(week >= 1 && week <= 8){
                            if(week%2 == 0){
                                if(str[5*k+2].indexOf("01-16") != -1 || str[5*k+2].indexOf("08") != -1){
                                    schedule[i][j] = str[5*k+1] + str[5*k+4];
                                    break;
                                }

                        }
                            else{
                                if(str[5*k+2].indexOf("01") != -1){
                                    schedule[i][j] = str[5*k+1] + str[5*k+4];
                                    break;
                                }
                            }
                        }
                        else if(week >= 9 && week <= 16){
                            if(week%2 == 0){
                                if(str[5*k+2].indexOf("16") != -1){
                                    schedule[i][j] = str[5*k+1] + str[5*k+4];
                                    break;
                                }
                            }
                            else{
                                if(str[5*k+2].indexOf("01-16") != -1 || str[5*k+2].indexOf("09") != -1 ){
                                    schedule[i][j] = str[5*k+1] + str[5*k+4];
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setday(String[] timelist,LinearLayout linerlayout) {
        System.out.println(111);
        int last = 0;
        for(int i = 0; i < 7; i++){
            System.out.println(timelist[i] + " nn");
                if (!timelist[i].equals("")) {
                    System.out.println(timelist[i] + " 12");

                    if (i == 0) {
                        setstyle(linerlayout, timelist[0], 128, 0);
                        last = 1;
                    }
                    else if (i > 0 && i <= 4) {
                        setstyle(linerlayout, timelist[i], 128, 128 * (i - last));
                        last = i + 1;
                    }
                    else if (i == 5) {
                        setstyle(linerlayout, timelist[i], 128, (128 * (i - last) + 64));
                        last = 6;
                    }
                    else{
                        if(last == 6)
                            setstyle(linerlayout, timelist[i], 64, 0);
                        else
                            setstyle(linerlayout, timelist[i], 64, (128 * (i - last) + 64));
                    }
                }
        }
    }


    private void setstyle(LinearLayout linearLayout, String string, int height, int margin) {
        TextView textView = new TextView(this);
        textView.setText(string);
        textView.setBackgroundColor(0x6AFFC1C1);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTopx(height));
        lp.setMargins(dpTopx(2), dpTopx(margin + 3), dpTopx(2), 0);
        textView.setLayoutParams(lp);
        linearLayout.addView(textView);

    }

    //将dp单位换算为px
    private int dpTopx(int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.getResources().getDisplayMetrics());
        return px;
    }
}
