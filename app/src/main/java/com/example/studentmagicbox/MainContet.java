package com.example.studentmagicbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainContet extends AppCompatActivity {

    private TextView name,unreadEmail,ecard,ipjj,netfee;
    private ImageView backto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contet);
        name=findViewById(R.id.master);
        unreadEmail=findViewById(R.id.unreademail);
        ecard=findViewById(R.id.ecard);
        ipjj=findViewById(R.id.ipjj);
        netfee=findViewById(R.id.netyue);
        backto=findViewById(R.id.backto);
        name.setText("欢迎您 "+MainActivity.emailBean.username);
        unreadEmail.setText("你有"+MainActivity.emailBean.new_email+"封未读邮件");
        ecard.setText("你的一卡通余额还剩"+MainActivity.emailBean.ecard_year+"元");
        ipjj.setText("你有"+MainActivity.emailBean.jjgp_ip+"个ip地址即将过期");
        netfee.setText("你的网费余额还剩"+MainActivity.emailBean.net_fee+"元");
        backto.setOnClickListener(e->{
            startActivity(new Intent(MainContet.this,MainActivity.class));
            finish();
        });
    }
}