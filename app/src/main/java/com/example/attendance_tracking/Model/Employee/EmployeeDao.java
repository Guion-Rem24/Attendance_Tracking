package com.example.attendance_tracking.Model.Employee;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Transaction;

import com.example.attendance_tracking.Model.EmployeeWorkTimes;

import java.util.List;

@Dao
public interface EmployeeDao {
    @Query("SELECT * FROM employee_table")
    public LiveData<List<Employee>> getEmployees();

    @Query("DELETE FROM employee_table")
    public void delete();

//    @Query("DELETE FROM employee_table WHERE id = employee.id")
//    public void delete(Employee employee);

    @Transaction
//    @Query("SELECT * FROM Employee")
    @Query("SELECT * FROM employee_table")
    LiveData<List<EmployeeWorkTimes>> getEmployeeWorkTimes();

    @Delete
    void delete(Employee employee);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Employee employee);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT fixed_time FROM employee_table WHERE id LIKE :id")
    String getFixedTime(int id);
}
