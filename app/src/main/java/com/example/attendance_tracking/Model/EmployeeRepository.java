package com.example.attendance_tracking.Model;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EmployeeRepository {
    LiveData<List<Employee>> allEmployees;

    EmployeeRepository newInstance(EmployeeDao employeeDao){
//        allEmployees = employeeDao
        return new EmployeeRepository();
    }

}
