package com.example.emsam.archcomp.model;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "user_table")
public class UserInfo
{
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    private String name;
    @NonNull
    private String email;
    private int age;

    public UserInfo(@NonNull String name, int age)
    {
        this.name = name;
        this.age = age;
        this.email = (name.replace(" ", ".") + "@email.com").toLowerCase();
    }

    public String getName()
    {
        return name;
    }

    public void setName(@NonNull String name)
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(@NonNull String email)
    {
        this.email = email;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString()
    {
        return String.format("Name  : %s\nEmail   :%s\nAge:     %d", name, email, age);
    }
}
