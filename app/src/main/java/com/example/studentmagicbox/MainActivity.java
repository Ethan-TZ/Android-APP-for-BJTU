package com.example.studentmagicbox;
import com.example.studentmagicbox.Beans.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {
    private final int LOADING=1;
    private static String loginname;
    private static String password;
    EditText userlogin;
    EditText userpassword;

    private TextView mBtnLogin;
    private View progress;
    private View mInputLayout;
    private float mWidth,mHeight;
    private LinearLayout mName,mPsw;

    private void InitView()
    {
        mBtnLogin=findViewById(R.id.main_btn_login);
        progress=findViewById(R.id.layout_progress);
        mInputLayout=findViewById(R.id.input_layout);
        mName=findViewById(R.id.input_layout_name);
        mPsw=findViewById(R.id.input_layout_psw);
        userlogin=findViewById(R.id.loginname);
        userpassword=findViewById(R.id.password);
    }



    /**
     * 输入框的动画效果
     *
     * @param view
     *            控件
     * @param w
     *            宽
     * @param h
     *            高
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }


    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }
    public static String getDefaultFilePath() {
        String filepath = "";
        File file = new File(Environment.getExternalStorageDirectory(),
                "userinfo.txt");
        if (file.exists()) {
            filepath = file.getAbsolutePath();
        } else {
            filepath = "不适用";
        }
        return filepath;
    }
    

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        InitView();
        initPython();
        String strsBuffer ="";
        try {
            // 判断是否存在SD
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File file = new File(Environment.getExternalStorageDirectory()
                        .getCanonicalPath() + "/userinfo.txt");
                // 判断是否存在该文件
                if (file.exists()) {
                    // 打开文件输入流
                    FileInputStream fileR = new FileInputStream(file);
                    BufferedReader reads = new BufferedReader(
                            new InputStreamReader(fileR));
                    String st = null;
                    while ((st = reads.readLine()) != null) {
                        strsBuffer+=st;
                    }
                    fileR.close();
                    String[]s=strsBuffer.split(" ");
                   userlogin.setText(s[0]);
                   userpassword.setText(s[1]);
                    password=s[1];
                } else {
                    Toast.makeText(this, "该目录下文件不存在", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
        }



        new LoadingThread().start();
        new EmailThread().start();
        new ClassRoomThread().start();
        new ScheduleThread().start();
        new FileThread().start();
        mBtnLogin.setOnClickListener(evt->
        {
            loginname=userlogin.getText().toString();
            password=userpassword.getText().toString();



            PyObject o=Python.getInstance().getModule("hello").callAttr("checkUser",loginname,password);
            Boolean success=o.toJava(Boolean.class);
            if(!success)
            {
                Toast.makeText(MainActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                return;
            }

            // 计算出控件的高与宽
            mWidth = mBtnLogin.getMeasuredWidth();
            mHeight = mBtnLogin.getMeasuredHeight();
            // 隐藏输入框
            mName.setVisibility(View.INVISIBLE);
            mPsw.setVisibility(View.INVISIBLE);

            inputAnimator(mInputLayout, mWidth, mHeight);

            FileIOHandler.sendEmptyMessageDelayed(1,0);
            patchHandler.sendEmptyMessageDelayed(1,0);
            classHandler.sendEmptyMessageDelayed(1,2000);
            scheduleHander.sendEmptyMessageDelayed(1,4000);
            emailHandler.sendEmptyMessageDelayed(1,6000);
        });
    }


    private void recovery()
    {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }

    public static String extract(String mid)
    {
        PyObject o = Python.getInstance().getModule("hello").callAttr("extract", mid);
        return o.toJava(String.class);
    }




    private Handler patchHandler,emailHandler,classHandler,scheduleHander,FileIOHandler;

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

    class FileThread extends Thread{
        @SuppressLint("HandlerLeak")
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            FileIOHandler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    try {
                        // 判断是否存在SD卡
                        if (Environment.getExternalStorageState().equals(
                                Environment.MEDIA_MOUNTED)) {
                            // 获取SD卡的目录
                            File sdDire = Environment.getExternalStorageDirectory();
                            FileOutputStream outFileStream = new FileOutputStream(
                                    sdDire.getCanonicalPath() + "/userinfo.txt");
                            outFileStream.write((loginname+" "+password).getBytes());
                            outFileStream.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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