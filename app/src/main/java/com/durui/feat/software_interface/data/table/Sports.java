package com.durui.feat.software_interface.data.table;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sports_table")
public class Sports {
    @PrimaryKey(autoGenerate = true)
    public Long sports_id;
    public String sports_name;
    public String sports_intro;
    public String sports_csv;

    public Sports(String sports_name, String sports_intro, String sports_csv) {
        this.sports_name = sports_name;
        this.sports_intro = sports_intro;
        this.sports_csv = sports_csv;
    }
}
