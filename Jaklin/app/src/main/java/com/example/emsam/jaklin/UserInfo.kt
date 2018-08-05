package com.example.emsam.jaklin

import android.annotation.SuppressLint

data class UserInfo(var name: String, var age: Int) {
    var id: Int = 0
    var email: String = (name.replace(" ", ".") + "@email.com").toLowerCase()

    @SuppressLint("DefaultLocale")
    override fun toString(): String {
        return String.format("Name  : %s\nEmail   :%s\nAge:     %d", name, email, age)
    }
}
