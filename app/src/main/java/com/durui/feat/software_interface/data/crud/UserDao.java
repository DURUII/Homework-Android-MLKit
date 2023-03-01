package com.durui.feat.software_interface.data.crud;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.durui.feat.software_interface.data.table.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user_table WHERE user_id=:userId")
    List<User> get(Long userId);

    @Query("SELECT * FROM user_table WHERE user_id=:userId AND user_passport=:pwd")
    List<User> get(Long userId, String pwd);

    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAll();

    @Query("DELETE FROM user_table")
    void clear();
}