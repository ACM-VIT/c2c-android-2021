package com.acmvit.c2c2021.model

data class User(var email: String,
                var name: String,
                var teamName: String) {
    constructor():this("", "", "")
}