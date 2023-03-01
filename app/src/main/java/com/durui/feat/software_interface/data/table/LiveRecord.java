package com.durui.feat.software_interface.data.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "live_record_table",
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "live_user_id"),
                @ForeignKey(entity = Sports.class, parentColumns = "sports_id", childColumns = "live_sports_id")
        })

public class LiveRecord {
    @PrimaryKey(autoGenerate = true)
    public Long live_id;

    @ColumnInfo(index = true)
    public Long live_user_id;

    @ColumnInfo(index = true)
    public Long live_sports_id;

    public Date live_time_start = new Date();

    public Date live_time_end = live_time_start;

    public Long live_sports_num;

    public Integer live_rate = -1;
}
