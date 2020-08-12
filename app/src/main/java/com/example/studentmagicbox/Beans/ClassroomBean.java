package com.example.studentmagicbox.Beans;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ClassroomBean {
    public List<String> RoomList[]=new ArrayList[7];

    public void setTimeinfo(String timeinfo) {
        this.timeinfo = timeinfo;
    }

    public String timeinfo;


    public ClassroomBean() {
        for(int i=0;i<=6;++i)
            RoomList[i]=new ArrayList<>();
    }

    public void add(int num,String name)
    {
        RoomList[num-1].add(name);
    }
}
