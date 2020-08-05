package com.example.studentmagicbox;

import java.util.ArrayList;
import java.util.List;

public class GradeBean {
    public float GPA;
    public float AVG;
    public List<GradeItem> grades= new ArrayList<>();
    public GradeBean(float GPA, float AVG) {
        this.GPA = GPA;
        this.AVG = AVG;
    }
    public void add_grade(GradeItem s)
    {
        grades.add(s);
    }

}
