package com.durui.feat.software_interface.data.table;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "state_table")
public class State {
    @PrimaryKey
    public Long state_id;
    public String state_name;

    public State(Long state_id, String state_name) {
        this.state_id = state_id;
        this.state_name = state_name;
    }
}
