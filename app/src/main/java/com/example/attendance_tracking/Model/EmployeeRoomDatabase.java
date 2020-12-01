package com.example.attendance_tracking.Model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {Employee.class}, version = 1, exportSchema = false)
public abstract class EmployeeRoomDatabase extends RoomDatabase {
    public abstract EmployeeDao employeeDao();
    private static EmployeeRoomDatabase instance = null;

//    private EmployeeRoomDatabase(){}
    public EmployeeRoomDatabase(){}

    // getInstance()
    static public EmployeeRoomDatabase
    getEmployeeDatabase(Context context){
        if(instance == null){
//            instance = new EmployeeRoomDatabase() {
//                @Override
//                public EmployeeDao employeeDao() {
//                    return null;
//                }
//
//                @NonNull
//                @Override
//                protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
//                    return null;
//                }
//
//                @NonNull
//                @Override
//                protected InvalidationTracker createInvalidationTracker() {
//                    return null;
//                }
//
//                @Override
//                public void clearAllTables() {
//
//                }
//            }

            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    EmployeeRoomDatabase.class,
                    "employee_database"
                ).build();
        }
        return instance;
    }
}
