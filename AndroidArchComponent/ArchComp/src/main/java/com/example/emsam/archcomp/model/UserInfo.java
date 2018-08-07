package com.example.emsam.archcomp.model;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "user_table")
public class UserInfo
{
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    private String name;
    @NonNull
    private String email;
    private int age;

    public UserInfo()
    {
    }

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

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString()
    {
        return String.format("Name  : %s\nEmail   :%s\nAge:     %d", name, email, age);
    }

    public static class Builder
    {
        private UserInfo user;

        public Builder()
        {
            user = new UserInfo();
        }

        public Builder setId(long id)
        {
            user.setId(id);
            return this;
        }
        public Builder setName(String name)
        {
            user.setName(name);
            return this;
        }
        public Builder setEmail(String email)
        {
            user.setEmail(email);
            return this;
        }
        public Builder setAge(int age)
        {
            user.setAge(age);
            return this;
        }

        public UserInfo build()
        {
            return user;
        }
    }
}
