package com.example.attendance_tracking.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {Employee.class}, version = 2, exportSchema = false)
public abstract class EmployeeRoomDatabase extends RoomDatabase {
    final static private String TAG = "EmployeeRoomDatabase";
    public abstract EmployeeDao employeeDao();

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(instance).execute();
                }
            };

    // getInstance()
    private static EmployeeRoomDatabase instance;
    static public EmployeeRoomDatabase getDatabase(Context context){
        Log.d(TAG, "[getDatabase]");
        if(instance == null){
            synchronized (EmployeeRoomDatabase.class) {
                // create Database
                Log.d(TAG,"Database create");
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        EmployeeRoomDatabase.class, "employee_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(sRoomDatabaseCallback)
                        .build();
            }
        }
        return instance;
    }

    /**
     * Populate the database in the background
     */
    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void> {
        private final EmployeeDao mDao;
        String[] classNames = {"test1","test2","test3"};

        PopulateDbAsync(EmployeeRoomDatabase db){ mDao = db.employeeDao(); }

        @Override
        protected Void doInBackground(final Void... params){
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created


            return null;
        }
    }
}
