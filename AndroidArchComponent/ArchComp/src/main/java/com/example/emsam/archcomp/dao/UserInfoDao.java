package com.example.emsam.archcomp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.emsam.archcomp.model.UserInfo;

import java.util.List;

@Dao
public interface UserInfoDao
{
    @Insert
    void insertUser(UserInfo userInfo);

    @Delete
    void deleteUser(UserInfo userInfo);

    @Query("DELETE FROM user_table")
    void deleteAllUsers();

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    LiveData<List<UserInfo>> getAllUsers();

    @Query("SELECT * FROM user_table WHERE age BETWEEN :min AND:max")
    LiveData<List<UserInfo>> getUsersInRangeOf(int min, int max);


}
