package com.example.attendance_tracking.View;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_tracking.Model.Employee;
import com.example.attendance_tracking.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 参考: https://qiita.com/naoi/items/f8a19d6278147e98bbc2
// AdapterでRecyclerViewに表示するItemのデータ管理とView管理を行う

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> {
    private List<Employee> listEmployees;
    private EmployeeListAdapter.OnItemClickListener listener = null;
    private LayoutInflater inflater;
    private final String TAG = "EmployeeAdapter";


    public static class
        // ViewHolderがRecyclerViewのItemのデータ管理？
        // 表示したいものが入る(はず)
    EmployeeViewHolder extends RecyclerView.ViewHolder {
        public TextView employeeName;
        public TextView assigned;
        public ImageButton deleteButton;
        private Employee employee = null;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            employeeName = (TextView) itemView.findViewById(R.id.textView_employeeName);
            assigned = (TextView) itemView.findViewById(R.id.text_assigned);
            deleteButton = (ImageButton) itemView.findViewById(R.id.button_delete_employee_item);
        }

        public void set(Employee employee){ this.employee = employee; }
        public Employee get() { return this.employee; }

    }

    public EmployeeListAdapter(List<Employee> employees){
        listEmployees = employees;
    }
    public EmployeeListAdapter(List<Employee> employees, Context context){
        listEmployees = employees;
        inflater = LayoutInflater.from(context);
    }
    public EmployeeListAdapter(Context context){
        Log.v(TAG," --- constructor ---");
        inflater = LayoutInflater.from(context);
        listEmployees = new ArrayList<>();
    }

    public void setOnItemClickListener(EmployeeListAdapter.OnItemClickListener listener){ this.listener = listener; }


    @NotNull
    @Override // 表示する行数+α分のViewHolderの生成
    public EmployeeListAdapter.EmployeeViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType){
//        View itemView = (View) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.recyclerview_employee, parent, false);
        ConstraintLayout itemView = (ConstraintLayout) inflater.inflate(R.layout.recyclerview_employee, parent, false);
        ImageButton button = itemView.findViewById(R.id.button_delete_employee_item);
//        ConstraintSet set = new ConstraintSet();
//        set.connect(button.getId(),ConstraintSet.END, parent.getId(), ConstraintSet.END);
//        itemView.setConstraintSet(set);
        EmployeeViewHolder viewHolder = new EmployeeViewHolder(itemView);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onClick(viewHolder);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void // 1行分のViewHolderに格納するデータを設定
    onBindViewHolder(EmployeeViewHolder holder, int position){
        Employee current = listEmployees.get(position);
        holder.set(current);
        String name = current.familyName + " " + current.firstName;
        holder.employeeName.setText(name); // TODO:とりあえずのFamilyName
        holder.assigned.setText((current.fulltime ? "正社員" : "アルバイト"));
    }

    public void
    setEmployees(List<Employee> employees){
        listEmployees = employees;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listEmployees.size();
    }

    static public interface OnItemClickListener {
        void onClick(EmployeeViewHolder viewHolder);
    }

}
