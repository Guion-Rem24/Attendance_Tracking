package com.example.attendance_tracking.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.attendance_tracking.Model.Employee;
import com.example.attendance_tracking.R;
import com.example.attendance_tracking.ViewModel.EmployeeHomeFragmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EmployeeHomeFragment extends Fragment {

    private static final String TAG = "HomeEmployeeFragment";

    private EditEmployeeActivity parent;
    private View base;
    private ViewPager2 viewPager;
    private EmployeeHomeFragmentViewModel viewModel;
    private EmployeeListAdapter adapter;
    private RecyclerView recyclerView;

    private FloatingActionButton addButton;

    public static EmployeeHomeFragment newInstance() { return new EmployeeHomeFragment(); }

    public EmployeeHomeFragment() {
        // Required empty public constructor
    }

    public static EmployeeHomeFragment newInstance(String param1, String param2) {
        EmployeeHomeFragment fragment = new EmployeeHomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "[onCreate]");
        parent = (EditEmployeeActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "[onCreateView]");

        base = inflater.inflate(R.layout.fragment_employee_home, container, false);
        findViews();
        setListeners();

        viewModel = new ViewModelProvider(this).get(EmployeeHomeFragmentViewModel.class);
        adapter = new EmployeeListAdapter(parent);
        viewModel.getAllEmployees().observe( getViewLifecycleOwner(), new Observer<List<Employee>>() {
            @Override
            public void onChanged(List<Employee> employee) {
                adapter.setEmployees(employee);
            }
        });

        recyclerView = (RecyclerView) base.findViewById(R.id.recyclerView_editEmployee);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(parent));
        recyclerView.setAdapter(adapter);
        // TODO: display employees in database

        return base;
    }

    public void findViews(){
        addButton = base.findViewById(R.id.button_addEmployee);
        viewPager = parent.getViewPager();
    }

    public void setListeners(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "addButton: [onClick]");
                viewPager.setCurrentItem(EditEmployeeActivity.FragNum.NewEmployee, false);
            }
        });
    }

}