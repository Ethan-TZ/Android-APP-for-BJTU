package com.example.studentmagicbox.Beans;

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

    public void add_grade(String lesson_year, String lesson_id, String lesson_name, String lesson_weight, String lesson_grade, String lesson_teacher)
    {
        grades.add(new GradeItem(lesson_year,lesson_id,lesson_name,lesson_weight,lesson_grade,lesson_teacher));
    }

    public class GradeItem {
        public String lesson_year;
        public String lesson_id;
        public String lesson_name;
        public String lesson_weight;
        public String lesson_grade;
        public String lesson_teacher;

        public String getLesson_year(){
            return lesson_year;
        }

        public String getLesson_name(){
            return lesson_name;
        }

        public String getLesson_weight(){
            return lesson_weight;
        }

        public String getLesson_grade(){
            return lesson_grade;
        }

        public String getLesson_teacher(){
            return lesson_teacher;
        }


        public GradeItem(String lesson_year, String lesson_id, String lesson_name, String lesson_weight, String lesson_grade, String lesson_teacher) {
            this.lesson_year = lesson_year;
            this.lesson_id = lesson_id;
            this.lesson_name = lesson_name;
            this.lesson_weight = lesson_weight;
            this.lesson_grade = lesson_grade;
            this.lesson_teacher = lesson_teacher;
        }




        @Override
        public String toString() {
            return "GradeItem{" +
                    "lesson_year='" + lesson_year + '\'' +
                    ", lesson_id='" + lesson_id + '\'' +
                    ", lesson_name='" + lesson_name + '\'' +
                    ", lesson_weight='" + lesson_weight + '\'' +
                    ", lesson_grade='" + lesson_grade + '\'' +
                    ", lesson_teacher='" + lesson_teacher + '\'' +
                    '}';
        }
    }

}