package com.durui.feat.software_interface.data.table;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "index_record_table")
public class IndexRecord {
    @PrimaryKey(autoGenerate = true)
    public Long record_id;
    public Date record_date = new Date();
    public Long record_user_id;
    public Double record_index_height;
    public Double record_index_weight;
}