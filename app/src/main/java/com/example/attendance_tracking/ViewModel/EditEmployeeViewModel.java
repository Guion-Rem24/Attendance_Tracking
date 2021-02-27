package com.example.attendance_tracking.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.attendance_tracking.Model.Employee;
import com.example.attendance_tracking.Model.EmployeeDao;
import com.example.attendance_tracking.Model.EmployeeRepository;
import com.example.attendance_tracking.Model.EmployeeRoomDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditEmployeeViewModel extends AndroidViewModel {
    private EmployeeRepository repo;
    private Application app;

    public LiveData<List<Employee>> mAllEmployees;


    public EditEmployeeViewModel(Application app){
        super(app);
//        EmployeeDao employeeDao = EmployeeRoomDatabase.getEmployeeDatabase(app).employeeDao();
        repo = new EmployeeRepository(app);
        mAllEmployees = repo.allEmployees;
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
