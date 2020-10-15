package com.example.studentmagicbox;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.studentmagicbox.Beans.GradeBean;

import java.util.ArrayList;
import java.util.List;

public class GradeAdapter extends BaseAdapter {
    Context context;
    private List<GradeBean.GradeItem> list=new ArrayList<>();
    public static final  int[] colors=new int[]{0x9866CCCC,0x00000000};//这里没有引用进去使用,只是简单引用数组运算
    public GradeAdapter(List<GradeBean.GradeItem> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View view = super.getView(position, convertView, parent);
        ViewHolder viewHolder=null;



        if (convertView == null) {
            //缓存
            viewHolder=new ViewHolder();
            //吧item布局转成view对象
            convertView=View.inflate(context,R.layout.grade_record,null);

            // 从view对象里面找到对应的textView
            viewHolder.lesson_year=convertView.findViewById(R.id.tv_lessonyear);
            viewHolder.lesson_id = convertView.findViewById(R.id.tv_lessonid);
            viewHolder.lesson_name=convertView.findViewById(R.id.tv_lesson);
            viewHolder.lesson_weight=convertView.findViewById(R.id.tv_lessonweight);
            viewHolder.lesson_grade = convertView.findViewById(R.id.tv_grade);
            viewHolder.lesson_teacher=convertView.findViewById(R.id.tv_lessonteacher);

            //converView和Viewholder进行关联
            convertView.setTag(viewHolder);
        }else{
            viewHolder  = (ViewHolder) convertView.getTag();
        }
        List<GradeBean.GradeItem> list=new ArrayList<>(MainActivity.gradeBean.grades);
        GradeBean.GradeItem item=list.get(position);
        if (item!=null){
            viewHolder.lesson_year.setText(item.getLesson_year());
            viewHolder.lesson_year.setTextColor(Color.WHITE);
            viewHolder.lesson_id.setText(item.getLesson_id());
            viewHolder.lesson_id.setTextColor(Color.WHITE);
            viewHolder.lesson_name.setText(item.getLesson_name());
            viewHolder.lesson_name.setTextColor(Color.WHITE);
            viewHolder.lesson_weight.setText(item.getLesson_weight());
            viewHolder.lesson_weight.setTextColor(Color.WHITE);
            viewHolder.lesson_grade.setText(item.getLesson_grade());
            viewHolder.lesson_grade.setTextColor(Color.WHITE);
            viewHolder.lesson_teacher.setText(item.getLesson_teacher());
            viewHolder.lesson_teacher.setTextColor(Color.WHITE);
        }
        if (position%2==0)
            convertView.setBackgroundColor(colors[0]);
        else
            convertView.setBackgroundColor(colors[1]);

        return convertView;
    }
    //使用viewHolder实现提高效率和复用
    public class ViewHolder{
        private TextView lesson_year;
        private TextView lesson_id;
        private TextView lesson_name;
        private TextView lesson_weight;
        private TextView lesson_grade;
        private TextView lesson_teacher;

    }
}
