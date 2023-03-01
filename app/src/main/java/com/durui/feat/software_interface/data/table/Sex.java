package com.durui.feat.software_interface.data.table;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sex_table")
public class Sex {
    @PrimaryKey
    public Long sex_id;
    public String sex_name;

    public Sex(Long sex_id, String sex_name) {
        this.sex_id = sex_id;
        this.sex_name = sex_name;
    }
}
