package com.acmvit.c2c2021.model

data class Sponsor(var imageUrl: String,
                   var link: String,
                   var name: String) {
    constructor():this("",  "", "")
}