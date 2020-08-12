package com.example.studentmagicbox.Beans;

public class ScheduleBean {
    public String [][]schedule=new String[7][];

    public ScheduleBean() {
        for(int i=0;i<7;++i)
        {
            schedule[i]=new String[7];
        }
    }
    //星期i，第j节
    public void set(int i,int j,String k)
    {
        schedule[i-1][j-1]=k;
    }
}
