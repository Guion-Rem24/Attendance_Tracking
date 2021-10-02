package com.example.attendance_tracking.View;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_tracking.Model.Employee.Employee;
import com.example.attendance_tracking.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// 参考: https://qiita.com/naoi/items/f8a19d6278147e98bbc2
// AdapterでRecyclerViewに表示するItemのデータ管理とView管理を行う

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> {
    private List<Employee> listEmployees;
    private EmployeeListAdapter.OnItemClickListener listener = null;
    private EmployeeListAdapter.OnDeleteButtonsClickListener listenerForDelete = null;
    private LayoutInflater inflater;
    private final String TAG = "EmployeeAdapter";


    public static class
        // ViewHolderがRecyclerViewのItemのデータ管理？
        // 表示したいものが入る(はず)
    EmployeeViewHolder extends RecyclerView.ViewHolder {
        public TextView employeeName;
        public TextView assigned;
        public ImageButton deleteButton;
        public ConstraintLayout background;
        public LinearLayout foreground;
//        private Employee employee = null;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            employeeName = (TextView) itemView.findViewById(R.id.textView_employeeName);
            assigned = (TextView) itemView.findViewById(R.id.text_assigned);
            background = (ConstraintLayout) itemView.findViewById(R.id.background_buttons);
            foreground = (LinearLayout) itemView.findViewById(R.id.item_employee_base);
//            deleteButton = (ImageButton) itemView.findViewById(R.id.button_delete_employee_item);
        }

//        public void set(Employee employee){ this.employee = employee; }
//        public Employee get() { return this.employee; }

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
    public void setOnDeleteButtonsClickListener(EmployeeListAdapter.OnDeleteButtonsClickListener listener){ this.listenerForDelete = listener; }
    public EmployeeListAdapter.OnItemClickListener getItemClickListener(){ return listener; }
    public EmployeeListAdapter.OnDeleteButtonsClickListener getDeleteButtonCLickListener() {return listenerForDelete;}

    @NotNull
    @Override // 表示する行数+α分のViewHolderの生成
    public EmployeeListAdapter.EmployeeViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType){
//        ConstraintLayout itemView = (ConstraintLayout) inflater.inflate(R.layout.recyclerview_employee, parent, false);
        FrameLayout itemView = (FrameLayout) inflater.inflate(R.layout.recyclerview_employee, parent, false);

        EmployeeViewHolder viewHolder = new EmployeeViewHolder(itemView);
        viewHolder.foreground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "[onClick]");
                if(listener != null) {
                    listener.onClick(viewHolder);
                }
            }
        });
        List<ImageButton> buttons = new ArrayList<>();
        buttons.add(viewHolder.background.findViewById(R.id.button_delete_left));
        buttons.add(viewHolder.background.findViewById(R.id.button_delete_right));
        for(ImageButton button : buttons){
            if(listenerForDelete!=null){
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listenerForDelete.onClick(viewHolder);
                    }
                });
            }
        }
        return viewHolder;
    }

    @Override
    public void // 1行分のViewHolderに格納するデータを設定
    onBindViewHolder(EmployeeViewHolder holder, int position){
        Employee current = listEmployees.get(position);
//        holder.set(current);
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
        return (listEmployees!=null?listEmployees.size():0);
    }

    public void remove(int position){
        listEmployees.remove(position);
        notifyItemRemoved(position);
    }

    static public interface OnItemClickListener {
        void onClick(EmployeeViewHolder viewHolder);
    }

    static public interface OnDeleteButtonsClickListener {
        void onClick(EmployeeViewHolder viewHolder);
    }

    public Employee getEmployee(int position){
        return listEmployees.get(position);
    }

}
