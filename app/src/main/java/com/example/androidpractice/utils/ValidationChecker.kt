package com.example.androidpractice.utils

object ValidationChecker {
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()
        return emailRegex.matches(email)
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
}