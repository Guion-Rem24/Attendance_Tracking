package com.example.attendance_tracking.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.attendance_tracking.Model.Employee;
import com.example.attendance_tracking.Model.EmployeeRoomDatabase;
import com.example.attendance_tracking.R;
import com.example.attendance_tracking.ViewModel.EditEmployeeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class EditEmployeeActivity extends AppCompatActivity {

    private EditEmployeeViewModel viewModel;
    private final String TAG = "EditEmployeeActivity";

    private Toolbar toolbar;
    private FloatingActionButton addButton;
    private RecyclerView recyclerView;
    private EmployeeListAdapter adapter;

    private ViewPager2 viewPager;
    private ViewPagerAdapter pagerAdapter;

    private static EditEmployeeFragment editEmployeeFragment;
    private static NewEmployeeFragment newEmployeeFragment;


    private static class FragNum{
        public static final int NewEmployee=0;
        public static final int EditEmployee=1;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        findViews();
        setListeners();

        // ViewPager2: Switching Fragments
        viewPager.setAdapter(pagerAdapter);

        // ToolBar
        toolbar.setTitle("従業員の編集");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel.getAllEmployees().observe(this, new Observer<List<Employee>>() {
            @Override
            public void onChanged(List<Employee> employee) {
                adapter.setEmployees(employee);
            }
        });

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

    private void findViews(){
        viewModel = new ViewModelProvider(this).get(EditEmployeeViewModel.class);
        pagerAdapter = new ViewPagerAdapter(this);
        viewPager = findViewById(R.id.viewPager_editEmployee);
        addButton = findViewById(R.id.button_addEmployee);
        toolbar = (Toolbar) findViewById(R.id.toolbar_editEmployee);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_editEmployee);
        adapter = new EmployeeListAdapter(this);
    }
    private void setListeners(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "addButton: [onClick]");
                viewPager.setCurrentItem(FragNum.NewEmployee);
                // TODO: viewPagerによるページ切り替え
            }
        });
    }

    public void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }
    public void setToolbarTitle(int id){
        toolbar.setTitle(id);
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {
        // extends FragmentStateAdapter
        public ViewPagerAdapter(FragmentActivity fa){
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position){
                case FragNum.NewEmployee:
                    newEmployeeFragment = new NewEmployeeFragment();
                    return newEmployeeFragment;
                case FragNum.EditEmployee:
                    editEmployeeFragment = new EditEmployeeFragment();
                    return editEmployeeFragment;
            }
            // position given as an argument do not match any FragNums,
            // returns HomeFragment
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}