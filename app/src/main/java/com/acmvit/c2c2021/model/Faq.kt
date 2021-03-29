package com.acmvit.c2c2021.model

data class Faq(val answer: String,
               val question: String) {
    constructor():this("","")
}