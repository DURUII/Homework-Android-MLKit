package com.durui.feat.software_interface.data.crud;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.durui.feat.software_interface.data.table.IndexRecord;
import com.durui.feat.software_interface.data.table.LiveRecord;
import com.durui.feat.software_interface.data.table.User;
import com.durui.feat.software_interface.ui.home.data.RankTuple;

import java.util.List;

@Dao
public interface OtherDao {
    @Query("SELECT * FROM user_table WHERE user_id=:user_id")
    LiveData<User> getLatestUser(Long user_id);

    @Query("SELECT * FROM index_record_table WHERE record_user_id=:user_id ORDER BY record_date DESC LIMIT 1")
    LiveData<IndexRecord> getLatestIndex(Long user_id);

    //FIXME 查询
    @Query("SELECT sex_name FROM user_table, sex_table WHERE sex_id=user_sex_id AND user_id=:user_id")
    String getUserSex(Long user_id);

    @Query("DELETE FROM index_record_table WHERE record_user_id=:user_id")
    void clearIndexRecordTableByUser(Long user_id);


    @Query("DELETE FROM live_record_table WHERE live_user_id=:user_id")
    void clearLiveRecordTableByUser(Long user_id);

    @Query("DELETE FROM user_table WHERE user_id=:user_id")
    void clearUserTableByUser(Long user_id);

    @Query("SELECT * FROM index_record_table WHERE record_user_id=:user_id ORDER BY record_date DESC LIMIT 8")
    LiveData<List<IndexRecord>> getRecnet8IndexRecord(Long user_id);

    @Query("SELECT * FROM live_record_table WHERE live_time_end=live_time_start AND live_user_id=:user_id ORDER BY live_time_end DESC LIMIT 1")
    LiveRecord getLatestLiveRecord(Long user_id);

    @Query("SELECT * FROM live_record_table WHERE live_user_id=:user_id ORDER BY live_sports_num DESC LIMIT 50")
    LiveData<List<LiveRecord>> getLatest50LiveRecord(Long user_id);

    @Query("SELECT user_name, user_avatar, MAX(live_sports_num)  as num_max FROM user_table,live_record_table WHERE user_id=live_user_id GROUP BY user_id ORDER BY live_sports_num DESC ")
    LiveData<List<RankTuple>> getRanking();
}
