package com.durui.feat.software_interface.data.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.durui.feat.software_interface.data.converter.Converters;
import com.durui.feat.software_interface.data.crud.IndexRecordDao;
import com.durui.feat.software_interface.data.crud.LiveRecordDao;
import com.durui.feat.software_interface.data.crud.OtherDao;
import com.durui.feat.software_interface.data.crud.SexDao;
import com.durui.feat.software_interface.data.crud.SportsDao;
import com.durui.feat.software_interface.data.crud.StateDao;
import com.durui.feat.software_interface.data.crud.UserDao;
import com.durui.feat.software_interface.data.table.IndexRecord;
import com.durui.feat.software_interface.data.table.LiveRecord;
import com.durui.feat.software_interface.data.table.Sex;
import com.durui.feat.software_interface.data.table.Sports;
import com.durui.feat.software_interface.data.table.State;
import com.durui.feat.software_interface.data.table.User;

@Database(
        entities = {
                LiveRecord.class,
                IndexRecord.class,
                Sex.class, Sports.class, State.class, User.class
        },
        version = 13,
        exportSchema = false)
@TypeConverters({Converters.class})
abstract public class WorkoutDatabase extends RoomDatabase {
    static private WorkoutDatabase INSTANCE = null;

    static public synchronized WorkoutDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            WorkoutDatabase.class,
                            "my_database"
                    ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();

            WorkoutDatabase db = INSTANCE;
        }

        return INSTANCE;
    }

    public abstract IndexRecordDao indexRecordDao();

    public abstract LiveRecordDao liveRecordDao();

    public abstract SexDao sexDao();

    public abstract SportsDao sportsDao();

    public abstract StateDao stateDao();

    public abstract UserDao userDao();

    public abstract OtherDao otherDao();
}
