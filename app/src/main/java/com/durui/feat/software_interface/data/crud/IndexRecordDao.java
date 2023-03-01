package com.durui.feat.software_interface.data.crud;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.durui.feat.software_interface.data.table.IndexRecord;

import java.util.List;

@Dao
public interface IndexRecordDao {
    @Insert
    void insert(IndexRecord indexRecord);

    @Update
    void update(IndexRecord indexRecord);

    @Delete
    void delete(IndexRecord indexRecord);

    @Query("SELECT * FROM index_record_table WHERE record_id=:recordId")
    IndexRecord get(Long recordId);

    @Query("SELECT * FROM index_record_table")
    LiveData<List<IndexRecord>> getAll();

    @Query("DELETE FROM index_record_table")
    void clear();
}
