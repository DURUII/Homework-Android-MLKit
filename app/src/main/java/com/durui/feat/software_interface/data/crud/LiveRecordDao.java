package com.durui.feat.software_interface.data.crud;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.durui.feat.software_interface.data.table.LiveRecord;

import java.util.List;

@Dao
public interface LiveRecordDao {
    @Insert
    void insert(LiveRecord liveRecord);

    @Update
    void update(LiveRecord liveRecord);

    @Delete
    void delete(LiveRecord liveRecord);

    @Query("SELECT * FROM live_record_table")
    LiveData<List<LiveRecord>> getAll();

    @Query("SELECT * " +
            "FROM live_record_table " +
            "WHERE live_user_id = :userId " +
            "ORDER BY live_id DESC")
    LiveData<List<LiveRecord>> getAllLiveRecordByUserId(Long userId);

    @Query("DELETE FROM live_record_table")
    void clear();

}
