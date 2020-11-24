package com.example.attendance_tracking.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import java.util.List;
import java.util.Vector;

@Dao
public abstract class EmployeeDao {
    @Query("SELECT * FROM employee_table")
    public abstract LiveData<List<Employee>> getEmployee();

    @Query("DELETE FROM employee_table")
    public abstract void delete();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert (Employee employee);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT fixed_time FROM employee_table WHERE id")
    public abstract String getFixedTime();
}
