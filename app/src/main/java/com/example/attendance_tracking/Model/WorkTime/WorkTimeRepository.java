package com.example.attendance_tracking.Model.WorkTime;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.attendance_tracking.Model.EmployeeWorkTimes;

import java.util.List;

public class WorkTimeRepository {
    private LiveData<List<WorkTime>> allWorkTimes;
    private WorkTimeDao mDao;
    private LiveData<List<EmployeeWorkTimes>> employeesWorkTimes;
    public WorkTimeRepository(Application app){
        WorkTimeRoomDatabase db = WorkTimeRoomDatabase.getDatabase(app);
        mDao = db.workTimeDao();
        allWorkTimes = mDao.getAllWorkTimes();
    }

}
