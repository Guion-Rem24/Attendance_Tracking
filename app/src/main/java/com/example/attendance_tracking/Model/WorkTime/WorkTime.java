package com.example.attendance_tracking.Model.WorkTime;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "work_time_table"
//        foreignKeys = @ForeignKey(entity = Employee.class,
//                                    parentColumns = "id",
//                                    childColumns = "user_id",
//                                    onDelete = ForeignKey.CASCADE)
)
public class WorkTime {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="index")
    private
    int index;

    @ColumnInfo(name="userId")
    private
    int userId;

    @ColumnInfo(name="workDay")
    private
    long workDay;

    @ColumnInfo(name="workTime")
    private
    String workTime;

    public WorkTime(int user_id){ this.userId = user_id; }
    public WorkTime(){}

    public int getIndex() {
        return index;
    }

    public int getUserId() {
        return userId;
    }

    public long getWorkDay() {
        return workDay;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setIndex(int id) {
        this.index = id;
    }

    public void setUserId(int user_id) {
        this.userId = user_id;
    }

    public void setWorkDay(long work_day) {
        this.workDay = work_day;
    }

    public void setWorkTime(String work_time) {
        this.workTime = work_time;
    }
}
