package com.durui.feat.software_interface.data.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "user_table",
        foreignKeys = {@ForeignKey(entity = Sex.class, parentColumns = "sex_id", childColumns = "user_sex_id"),
                @ForeignKey(entity = State.class, parentColumns = "state_id", childColumns = "user_state_id")}
)
public class User {
    @PrimaryKey
    public Long user_id;
    public String user_passport;
    @ColumnInfo(index = true)
    public Long user_sex_id = 0L;
    public Date user_birth;
    @ColumnInfo(index = true)
    public Long user_state_id = 0L;
    public Date user_register = new Date();
    public String user_avatar;
    public String user_name;
    public String user_sign;
}
