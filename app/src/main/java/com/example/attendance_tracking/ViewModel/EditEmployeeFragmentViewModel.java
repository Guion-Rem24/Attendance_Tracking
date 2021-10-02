package com.example.attendance_tracking.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.attendance_tracking.Model.Employee.Employee;
import com.example.attendance_tracking.Model.Employee.EmployeeRepository;
import com.example.attendance_tracking.Model.EmployeeWorkTimes;

import java.util.ArrayList;
import java.util.List;

public class EditEmployeeFragmentViewModel extends AndroidViewModel {
    private EmployeeRepository repo;
    private Application app;

    public LiveData<List<Employee>> mAllEmployees;
    public LiveData<List<EmployeeWorkTimes>> AllEmployeeWorkTimes;


    public EditEmployeeFragmentViewModel(Application app){
        super(app);
        this.app = app;
//        EmployeeDao employeeDao = EmployeeRoomDatabase.getEmployeeDatabase(app).employeeDao();
        repo = new EmployeeRepository(app);
        mAllEmployees = repo.getAllEmployees();
        AllEmployeeWorkTimes = repo.getAllEmployeeWorkTimes();

    }

    public LiveData<List<Employee>>
    getAllEmployees(){
        return repo.getAllEmployees();
    }

    public List<String>
    getAllNames(List<Employee> employees){
        return new ArrayList<>(repo.getAllEmployeeNames(employees).values());
    }

}
