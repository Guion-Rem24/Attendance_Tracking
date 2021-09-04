package com.example.attendance_tracking.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.attendance_tracking.Model.Employee;
import com.example.attendance_tracking.Model.EmployeeRepository;

import java.util.List;

public class NewEmployeeFragmentViewModel extends AndroidViewModel {

    private static final String TAG = "NewEmployeeViewModel";
    private EmployeeRepository employeeRepository;
    private LiveData<List<Employee>> mAllEmployees;


    public NewEmployeeFragmentViewModel(@NonNull Application application) {
        super(application);
        employeeRepository = new EmployeeRepository(application);
        mAllEmployees = employeeRepository.getAllEmployees();
    }

    public LiveData<List<Employee>> getAllEmployees() {
        return mAllEmployees;
    }

    public void insert(Employee e) { employeeRepository.insert(e); }
}
