package com.example.emsam.archcomp.dao;

import com.example.emsam.archcomp.model.UserInfo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserInfoDao
{
    @Insert
    long[] insertUser(UserInfo... users);

    @Delete
    void deleteUser(UserInfo... users);

    @Query("DELETE FROM user_table")
    void deleteAllUsers();

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    DataSource.Factory<Integer, UserInfo> getAllUsers();

    @Query("SELECT * FROM user_table WHERE age BETWEEN :min AND:max")
    LiveData<List<UserInfo>> getUsersInRangeOf(int min, int max);

    @Query("SELECT * FROM user_table ORDER BY id ASC LIMIT:offset, :len")
    LiveData<List<UserInfo>> getUsers(int offset, int len);
}
