package com.example.studentmagicbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Xml;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.nio.charset.Charset;

public class EmailShow extends AppCompatActivity {
    private ImageView backto;
    private LollipopFixedWebView emailcontent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_show);
        backto=findViewById(R.id.backto);
        emailcontent=findViewById(R.id.Emailcontent);
        backto.setOnClickListener(e->{
            startActivity(new Intent(EmailShow.this,EmailUI.class));
            finish();
        });
        emailcontent.loadDataWithBaseURL(null,
                MainActivity.extract(EmailUI.mid),"text/html",
                "charset=UTF-8",null);
    }
}