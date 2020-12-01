package com.example.attendance_tracking.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.attendance_tracking.Model.Employee;
import com.example.attendance_tracking.Model.EmployeeRoomDatabase;
import com.example.attendance_tracking.R;
import com.example.attendance_tracking.ViewModel.EditEmployeeViewModel;

import java.util.ArrayList;
import java.util.List;


public class EditEmployeeActivity extends AppCompatActivity {

    private EditEmployeeViewModel viewModel;
    private Object EditEmployeeViewModel;
    private final String TAG = "EditEmployeeActivity";
    private EmployeeRoomDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        // instead of Singleton
        db = EmployeeRoomDatabase.getEmployeeDatabase(this);

        // ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_editEmployee);
        toolbar.setTitle("従業員の編集");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_editEmployee);
        EmployeeListAdapter adapter = new EmployeeListAdapter(this);

        viewModel = new ViewModelProvider(this).get(EditEmployeeViewModel.class);
//        viewModel.listEmployees.observe(this, this::observe(it));
        final Observer<List<Employee>> NameObserver = new Observer<List<Employee>>() {
            @Override
            public void onChanged(List<Employee> employees) {
                adapter.setEmployees(viewModel.getAllEmployees(employees));
                // TODO:getAllNames @return List<String>
                //      setEmployees( List<Employee> )
                //      結果的にどこで表示しているかでどっちに合わせるか決まってくる
                //      おそらく，@return List<Employee> でよい？
            }
        };

        if(viewModel.listEmployees != null){
            Toast.makeText(getApplicationContext(), "'listEmployees' is not NULL...", Toast.LENGTH_LONG).show();
            viewModel.listEmployees.observe(this, NameObserver);
        }else{
            Toast.makeText(getApplicationContext(), "'listEmployees' is NULL...", Toast.LENGTH_LONG).show();
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Log.v(TAG, "---- move HOME ----");
            finish();
            return true;
        }

        switch(id){
            case R.id.optionsMenu_Pref:
                Toast.makeText(getApplicationContext(),"'設定' is selected...", Toast.LENGTH_LONG).show();
                Log.d(TAG, "...onOptionsItemSelected is active");
                Intent intent = new android.content.Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.optionsMenu_help:
                Toast.makeText(getApplicationContext(),"'ヘルプ' is selected...", Toast.LENGTH_LONG).show();
                return true;
            case R.id.optionsMenu_info:
                Toast.makeText(getApplicationContext(), "'お知らせ' is selected...", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "'action_settings' is selected...", Toast.LENGTH_LONG).show();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
}