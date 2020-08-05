package com.example.studentmagicbox;

public class GradeItem {
    public String lesson_year;
    public String lesson_id;
    public String lesson_name;
    public String lesson_weight;
    public String lesson_grade;
    public String lesson_teacher;

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
