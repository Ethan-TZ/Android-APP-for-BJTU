package com.example.studentmagicbox;
import com.example.studentmagicbox.Beans.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;


public class MainActivity extends AppCompatActivity {
    private final int LOADING=1;
    private String loginname;
    private String password;
    EditText userlogin;
    EditText userpassword;
    Button check;
    private int  SuccessCount=0;

    private void SkipTo()
    {
        if(++SuccessCount==4)
        {
            startActivity(new Intent(MainActivity.this,MainContet.class));
            finish();
        }
    }

    private void initPython() {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
            Python py = Python.getInstance();
            py.getModule("sys").get("path").callAttr("append", getFilesDir().getAbsolutePath());
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPython();
        setContentView(R.layout.activity_main);
        userlogin=(EditText)findViewById(R.id.userlogin);
        userpassword=(EditText)findViewById(R.id.userpassword);
        check=findViewById(R.id.check);
        new LoadingThread().start();
        new EmailThread().start();
        new ClassRoomThread().start();
        new ScheduleThread().start();
        check.setOnClickListener(evt->
        {
            loginname=userlogin.getText().toString();
            password=userpassword.getText().toString();
            patchHandler.sendEmptyMessageDelayed(1,0);
            classHandler.sendEmptyMessageDelayed(1,3000);
            scheduleHander.sendEmptyMessageDelayed(1,6000);
            emailHandler.sendEmptyMessageDelayed(1,9000);
        });
    }





    private Handler patchHandler,emailHandler,classHandler,scheduleHander;

    public static ClassroomBean classroomBean;
    public static EmailBean emailBean;
    public static GradeBean gradeBean;
    public static ScheduleBean scheduleBean;

    class LoadingThread extends  Thread {
        @SuppressLint("HandlerLeak")
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            patchHandler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    PyObject o = Python.getInstance().getModule("hello").callAttr("getgrade", loginname, password);
                    gradeBean = o.toJava(GradeBean.class);
                    Toast.makeText(MainActivity.this, Float.toString(gradeBean.GPA), Toast.LENGTH_LONG).show();
                    System.out.println("成绩载入成功");
                    SkipTo();
                }
            };
            Looper.loop();
        }
    }


    class EmailThread extends Thread{
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            emailHandler=new Handler()
            {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    PyObject o=Python.getInstance().getModule("hello").callAttr("getMail_and_Information",loginname,password);
                    emailBean= o.toJava(EmailBean.class);
                    for (EmailBean.EmailItem x:emailBean.EmailList)
                        System.out.println(x);
                    System.out.println(emailBean.ecard_year);
                    System.out.println("邮件载入成功！");
                    SkipTo();
                }
            };
            Looper.loop();
        }
    }

    class ClassRoomThread extends Thread{
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            classHandler=new Handler(){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    PyObject o=Python.getInstance().getModule("hello").callAttr("getClassRoom",loginname,password);
                    classroomBean=o.toJava(ClassroomBean.class);
                    for(int i=0;i<=6;++i)
                    {
                        System.out.println("第"+i+"节："+classroomBean.RoomList[i].size());
                    }
                    System.out.println(classroomBean.timeinfo);
                    System.out.println("教室资源载入成功！");
                    SkipTo();
                }
            };
            Looper.loop();
        }
    }

    class ScheduleThread extends Thread{
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            scheduleHander=new Handler()
            {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    PyObject o=Python.getInstance().getModule("hello").callAttr("getSchedule",loginname,password);
                    scheduleBean=o.toJava(ScheduleBean.class);
                    for(int i=0;i<=6;++i)
                        for(int j=0;j<=6;++j)
                            System.out.println(i+" "+j+scheduleBean.schedule[i][j]);
                        System.out.println("课表载入成功");
                        SkipTo();
                }
            };
            Looper.loop();
        }
    }

}