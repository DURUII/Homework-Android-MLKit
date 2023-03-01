package com.durui.feat.software_interface.data.crud;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.durui.feat.software_interface.data.table.State;

import java.util.List;

@Dao
public interface StateDao {
    @Insert
    void insert(State state);

    @Update
    void update(State state);

    @Delete
    void delete(State state);

    @Query("SELECT * FROM state_table")
    LiveData<List<State>> getAll();

    @Query("DELETE FROM state_table")
    void clear();
}
