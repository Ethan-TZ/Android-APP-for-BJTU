package com.example.studentmagicbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studentmagicbox.Beans.EmailBean;

import java.util.ArrayList;

public class EmailUI extends AppCompatActivity {
    private EmailBean emails=MainActivity.emailBean;
    private ListView ems;
    private TextView cur_pos,mailsum;
    private ImageView backto,backemail,tomail;
    private ArrayList<EmailBean.EmailItem> cur_email=new ArrayList<>();
    private static int page=1;
    private EmailAdp adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_u_i);
        backto=findViewById(R.id.backto);
        backemail=findViewById(R.id.backemail);
        tomail=findViewById(R.id.toemail);
        cur_pos=findViewById(R.id.curpos);
        mailsum=findViewById(R.id.mailnum);
        int sum=emails.EmailList.size()/10+1;
        show(page);
        mailsum.setText('/'+Integer.toString(sum));
        backto.setOnClickListener(e->{
            startActivity(new Intent(EmailUI.this,MainContet.class));
            finish();
        });
        backemail.setOnClickListener(e->{
            page=page==1?1:page-1;
            show(page);
        });
        tomail.setOnClickListener(e->{
            page=page==sum?sum:page+1;
            show(page);
        });
        ems=findViewById(R.id.Emails);
        adp=new EmailAdp(this);
        ems.setAdapter(adp);
        ems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                transshow(cur_email.get(i).mid);
            }
        });
    }

    public static String mid;
    private void transshow(String mids)
    {
        mid=mids;
        startActivity(new Intent(EmailUI.this,EmailShow.class));
        finish();
    }


    private void show(int i)
    {
        cur_email.clear();
        cur_pos.setText(Integer.toString(i));
        if(adp!=null)
        adp.notifyDataSetChanged();
        for(int pos=10*(i-1);pos<Math.min(10*i,emails.EmailList.size());pos++)
            cur_email.add(emails.EmailList.get(pos));
    }

    private class EmailAdp extends BaseAdapter
    {
        private LayoutInflater mInflater;
        public EmailAdp(Context context)
        {
            this.mInflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int i) {
            return cur_email.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view==null)
            {
                view=mInflater.inflate(R.layout.emailitems,null);
                holder=new ViewHolder();
                holder.title=view.findViewById(R.id.title);
                holder.subject=view.findViewById(R.id.subject);
                view.setTag(holder);
            }
            else
                holder=(ViewHolder) view.getTag();
            holder.title.setText(cur_email.get(i).subject);
            holder.subject.setText(cur_email.get(i).sender_name);
            return view;
        }
        public final class ViewHolder
        {
            public TextView title;
            public TextView subject;
        }
    }
}