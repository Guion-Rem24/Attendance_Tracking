package com.example.attendance_tracking.Model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.attendance_tracking.Model.Employee.Employee;
import com.example.attendance_tracking.Model.WorkTime.WorkTime;

import java.util.List;

// TODO: なぜかエラーが消えた workTimesを一旦消してからBuild -> コメントを外し再度Build
public class EmployeeWorkTimes {
    @Embedded public Employee employee;
    @Relation(
            parentColumn = "id",
            entityColumn = "userId"
    )
    public List<WorkTime> workTimes;
}
