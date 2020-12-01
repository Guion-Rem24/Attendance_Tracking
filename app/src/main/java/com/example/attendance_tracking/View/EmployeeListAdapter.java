package com.example.attendance_tracking.View;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private LayoutInflater inflater;
    private final String TAG = "EmployeeAdapter";


    public static class
        // ViewHolderがRecyclerViewのItemのデータ管理？
        // 表示したいものが入る(はず)
    EmployeeViewHolder extends RecyclerView.ViewHolder {
        public TextView employeeName;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            employeeName = (TextView) itemView.findViewById(R.id.textView_employeeName);
        }

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


    @NotNull
    @Override // 表示する行数+α分のViewHolderの生成
    public EmployeeListAdapter.EmployeeViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType){
//        View itemView = (View) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.recyclerview_employee, parent, false);
        View itemView = inflater.inflate(R.layout.recyclerview_employee, parent, false);
        return new EmployeeViewHolder(itemView);
    }

    @Override
    public void // 1行分のViewHolderに格納するデータを設定
    onBindViewHolder(EmployeeViewHolder holder, int position){
        Employee current = listEmployees.get(position);
        holder.employeeName.setText(current.familyName); // TODO:とりあえずのFamilyName
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
}
