package com.example.tenakatauniversity.Model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Student.class}, version = 1, exportSchema = false)
public abstract class MyAppDatabase extends RoomDatabase {

    private static MyAppDatabase MyAppDB = null;

    public abstract MyDao mMyDao();

    public static synchronized MyAppDatabase getDBInstance(Context context) {
        if (MyAppDB == null) {
            MyAppDB = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MyAppDatabase.class,
                    "student19b2"
            )
                    .allowMainThreadQueries()
                    .build();

        }
        return MyAppDB;
    }
}
