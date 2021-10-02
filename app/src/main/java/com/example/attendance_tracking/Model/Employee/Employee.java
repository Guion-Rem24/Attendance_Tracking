package com.example.attendance_tracking.Model.Employee;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

//@Fts4
@Entity(
//        tableName = "employee_table",
        indices = {
//        @Index("name"), なぜかエラーになる　参考: https://developer.android.com/training/data-storage/room/defining-data?hl=ja#column-indexing
                    @Index(value = {"first_kana", "family_kana"})}
        )
public class Employee
{
    @PrimaryKey @ColumnInfo(name = "id")
    int id;

    @ColumnInfo(name = "first_name")
    public String firstName;
    @ColumnInfo(name = "first_kana")
    public String firstKana;
    @ColumnInfo(name = "family_name")
    public String familyName;
    @ColumnInfo(name = "family_kana")
    public String familyKana;
    @ColumnInfo(name = "fixed_time")
    public String fixedTime;
    @ColumnInfo(name = "assigned")
    public boolean fulltime;

    public Employee() {}
    public Employee(int __id) { id = __id; }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() { return id; }
}
