package com.example.studentmagicbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmagicbox.Beans.GradeBean;

import java.util.ArrayList;
import java.util.List;

public class GradeActivity extends AppCompatActivity {
    private ImageView backto;
    private List<GradeBean.GradeItem> grade;
    private GradeAdapter Adapter;
    private ListView gradelist;
    private TextView GPA,AVG;
    public static String thisyear="无";
    private void initview() {
        backto=findViewById(R.id.backto);
        gradelist=(ListView)findViewById(R.id.grade_list);
        GPA=(TextView) findViewById(R.id.GPA);
        AVG=(TextView) findViewById(R.id.AVG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        initview();
        GPA.setText(String.valueOf("GPA:"+MainActivity.gradeBean.GPA));
        AVG.setText(String.valueOf("AVG"+MainActivity.gradeBean.AVG));

        grade=new ArrayList<>(MainActivity.gradeBean.grades);
        Adapter = new GradeAdapter(grade, this);
        //Adapter.notifyDataSetChanged();
        gradelist.setAdapter(Adapter);

        backto.setOnClickListener(e->{
            startActivity(new Intent(GradeActivity.this,MainContet.class));
            finish(); });

}
}
