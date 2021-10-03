package com.example.attendance_tracking.Model.WorkTime;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WorkTimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WorkTime time);
//    @Delete
    @Query("DELETE FROM work_time_table")
    void deleteAll();
    @Query("DELETE FROM work_time_table WHERE `index` LIKE :id ")
    void delete(int id);

    @Query("SELECT * FROM work_time_table")
    public LiveData<List<WorkTime>> getAllWorkTimes();
    @Query("SELECT * FROM work_time_table WHERE workDay LIKE :workDay")
    public LiveData<List<WorkTime>> getAllWorkTimesAt(long workDay);

}
