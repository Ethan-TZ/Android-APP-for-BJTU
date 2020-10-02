package com.example.studentmagicbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class TimeTableActivity extends AppCompatActivity {
    private ImageView backto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);


        backto=findViewById(R.id.backto);
        backto.setOnClickListener(e->{
            startActivity(new Intent(TimeTableActivity.this,MainContet.class));
            finish(); });


    }
}
