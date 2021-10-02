package com.example.attendance_tracking.Model.Employee;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.attendance_tracking.Model.EmployeeWorkTimes;
import com.example.attendance_tracking.Model.EmployeeWorkTimesDao;
import com.example.attendance_tracking.Model.WorkTime.WorkTimeDao;
import com.example.attendance_tracking.Model.WorkTime.WorkTimeRoomDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmployeeRepository {
    private LiveData<List<Employee>> allEmployees;
    private Map<Employee, String> allNamesMap;
    private EmployeeDao employeeDao;
    private WorkTimeDao workTimeDao;

    private EmployeeWorkTimesDao _dao;

    private LiveData<List<Employee>> mAllEmployees;
    private LiveData<List<EmployeeWorkTimes>> AllEmployeeWorkTimes;

    public EmployeeRepository(Application app){
        WorkTimeRoomDatabase w_db = WorkTimeRoomDatabase.getDatabase(app);
        workTimeDao = w_db.workTimeDao();
        EmployeeRoomDatabase e_db = EmployeeRoomDatabase.getDatabase(app);
        employeeDao = e_db.employeeDao();
//        _dao = e_db.workTimeDao();
        mAllEmployees = employeeDao.getEmployees();
        AllEmployeeWorkTimes = employeeDao.getEmployeeWorkTimes();
//        _dao = new EmployeeWorkTimesDao();
    }

    public Map<Employee, String>
    getAllEmployeeNames(List<Employee> employees){
        employees.forEach(this::inputName);
        return allNamesMap;
    }
    public LiveData<List<Employee>>
    getAllEmployees(){ return mAllEmployees; }

    public LiveData<List<EmployeeWorkTimes>>
    getAllEmployeeWorkTimes() {
        return AllEmployeeWorkTimes;
    }

    public List<Employee>
    getAllEmps(List<Employee> employees){
        employees.forEach(this::inputName);
        return new ArrayList<>(allNamesMap.keySet());
    }

    private void inputName(Employee it) {
        allNamesMap.put(it, it.familyName + " " + it.firstName);
    }
    public void insert(Employee e){ new insertAsyncTask(employeeDao).execute(e); }
    public void delete(Employee e){ new deleteAsyncTask(employeeDao).execute(e); }

    private static class insertAsyncTask extends AsyncTask<Employee, Void, Void> {
        private EmployeeDao dao;
        insertAsyncTask(EmployeeDao Dao){ dao = Dao; }
        @Override
        protected Void doInBackground(Employee... params) {
            dao.insert(params[0]);
            return null;
        }
    }
    private static class deleteAsyncTask extends AsyncTask<Employee, Void, Void>{
        private EmployeeDao dao;
        deleteAsyncTask(EmployeeDao Dao){ dao = Dao; }
        @Override
        protected Void doInBackground(Employee... params) {
            dao.delete(params[0]);
            return null;
        }
    }
}
