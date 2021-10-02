package com.example.attendance_tracking.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.attendance_tracking.Model.Employee.Employee;
import com.example.attendance_tracking.Model.Employee.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

public class EmployeeHomeFragmentViewModel extends AndroidViewModel {
    private EmployeeRepository repo;
    private Application app;

    public LiveData<List<Employee>> mAllEmployees;


    public EmployeeHomeFragmentViewModel(Application app){
        super(app);
//        EmployeeDao employeeDao = EmployeeRoomDatabase.getEmployeeDatabase(app).employeeDao();
        repo = new EmployeeRepository(app);
        mAllEmployees = repo.getAllEmployees();
    }

    public void deleteEmployee(Employee employee){ repo.delete(employee); }

    public LiveData<List<Employee>>
    getAllEmployees(){
        return repo.getAllEmployees();
    }

    public List<String>
    getAllNames(List<Employee> employees){
        return new ArrayList<>(repo.getAllEmployeeNames(employees).values());
    }

}
