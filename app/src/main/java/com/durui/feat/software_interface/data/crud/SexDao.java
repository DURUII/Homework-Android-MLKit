package com.durui.feat.software_interface.data.crud;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.durui.feat.software_interface.data.table.Sex;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface SexDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Sex sex);

    @Update
    void update(Sex sex);

    @Update
    ListenableFuture<Integer> update(List<Sex> sexes);

    @Delete
    void delete(Sex sex);

    @Delete
    ListenableFuture<Integer> delete(List<Sex> sexes);

    @Query("SELECT * FROM sex_table")
    ListenableFuture<List<Sex>> getAll();

    @Query("DELETE FROM sex_table")
    void clear();
}
