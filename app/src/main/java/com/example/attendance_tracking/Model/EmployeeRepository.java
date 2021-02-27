package com.example.attendance_tracking.Model;

import android.app.Application;
import android.content.ContentResolver;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeRepository {
    public LiveData<List<Employee>> allEmployees;
    private Map<Employee, String> allNamesMap;
    private EmployeeDao mDao;
    private LiveData<List<Employee>> mAllEmployees;

    public EmployeeRepository(Application app){
        EmployeeRoomDatabase db = EmployeeRoomDatabase.getDatabase(app);
        mDao = db.employeeDao();
        mAllEmployees = mDao.getEmployees();
    }

    public Map<Employee, String>
    getAllEmployeeNames(List<Employee> employees){
        employees.forEach(this::inputName);
        return allNamesMap;
    }
    public LiveData<List<Employee>>
    getAllEmployees(){ return mAllEmployees; }

    public List<Employee>
    getAllEmps(List<Employee> employees){
        employees.forEach(this::inputName);
        return new ArrayList<>(allNamesMap.keySet());
    }

    private void inputName(Employee it) {
        allNamesMap.put(it, it.familyName + " " + it.firstName);
    }
}
