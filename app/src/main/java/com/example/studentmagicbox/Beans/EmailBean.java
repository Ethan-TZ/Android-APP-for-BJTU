package com.example.studentmagicbox.Beans;

import java.util.ArrayList;
import java.util.List;

public class EmailBean {
    public List<EmailItem> EmailList=new ArrayList<>();
    //        jjgq_ip:15天内过期的ip
//        ip_count:名下共有ip地址
//        net_fee:网费
//        ecard_year:一卡通余额
    public String jjgp_ip,
            ip_count,net_fee,ecard_year,new_email;

    public void add(String mid, String sender_name, String subject, String is_read)
    {
        EmailList.add(new EmailItem(mid,sender_name,subject,is_read));
    }


    public void set(String jjgp_ip,String ip_count,String net_fee,String ecard_year,String new_email)
    {
        this.jjgp_ip=jjgp_ip;
        this.ip_count=ip_count;
        this.net_fee=net_fee;
        this.ecard_year=ecard_year;
        this.new_email=new_email;
    }

    public class EmailItem {
        private String mid;
        public String sender_name;
        public String subject;
        public String is_read;

        public EmailItem(String mid, String sender_name, String subject, String is_read) {
            this.mid = mid;
            this.sender_name = sender_name;
            this.subject = subject;
            this.is_read = is_read;
        }

        @Override
        public String toString() {
            return "EmailItem{" +
                    "mid='" + mid + '\'' +
                    ", sender_name='" + sender_name + '\'' +
                    ", subject='" + subject + '\'' +
                    ", is_read='" + is_read + '\'' +
                    '}';
        }
    }

}

