package com.example.emsam.archcomp;

import android.annotation.SuppressLint;

public class UserData
{
    private String name;
    private int age;
    private String email;

    public UserData(String name, int age)
    {
        this.name = name;
        this.age = age;
        this.email = (name.replace(" ", ".") + "@email.com").toLowerCase();
    }

    public String getName()
    {
        return name;
    }

    public int getAge()
    {
        return age;
    }

    public String getEmail()
    {
        return email;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString()
    {
        return String.format("Name  : %s\nEmail   :%s\nAge:     %d", name, email, age);
    }
}
