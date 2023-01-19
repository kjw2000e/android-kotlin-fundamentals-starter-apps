package com.example.android.devbyteviewer.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class Room2 extends RoomDatabase {
    private static Room2 INSTANCE;
    private final String db_name = "db_devbytes";

    public static Room2 getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (Room2.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, Room2.class, "db_name").build();
                }
            }
        }
        return INSTANCE;
    }
}
