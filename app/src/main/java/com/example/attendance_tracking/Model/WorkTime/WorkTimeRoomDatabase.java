package com.example.attendance_tracking.Model.WorkTime;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {WorkTime.class}, version=1, exportSchema = false)
public abstract class WorkTimeRoomDatabase extends RoomDatabase {
    final static private String TAG = "WorkTimeRoomDatabase";
    public abstract WorkTimeDao workTimeDao();

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDBAsync(instance).execute();
                }
            };


    private static WorkTimeRoomDatabase instance;
    static public WorkTimeRoomDatabase getDatabase(Context context){
        Log.d(TAG, "[getDatabase]");
        if(instance == null){
            synchronized (WorkTimeRoomDatabase.class){
                Log.d(TAG, "Database create");
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        WorkTimeRoomDatabase.class, "work_time_table")
                        .fallbackToDestructiveMigration()
                        .addCallback(sRoomDatabaseCallback)
                        .build();
            }
        }
        Log.d(TAG,"...Done");
        return instance;
    }

    /**
     * Populate the database in the background
     */
    private static class PopulateDBAsync extends AsyncTask<Void,Void,Void> {
        private final WorkTimeDao dao;
        PopulateDBAsync(WorkTimeRoomDatabase instance ){ this.dao = instance.workTimeDao(); }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            return null;
        }
    }
}
