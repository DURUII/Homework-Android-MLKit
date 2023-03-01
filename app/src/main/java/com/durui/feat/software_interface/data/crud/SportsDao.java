package com.durui.feat.software_interface.data.crud;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.durui.feat.software_interface.data.table.Sports;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface SportsDao {
    @Insert
    void insert(Sports sports);

    @Update
    void update(Sports sports);

    @Delete
    void delete(Sports sports);

    @Query("SELECT * FROM sports_table")
    LiveData<List<Sports>> getAll();

    @Query("SELECT * FROM sports_table")
    ListenableFuture<List<Sports>> getAllListenable();

    @Query("DELETE FROM sports_table")
    void clear();
}
