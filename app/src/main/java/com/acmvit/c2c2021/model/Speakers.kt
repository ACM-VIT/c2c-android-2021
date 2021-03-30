package com.acmvit.c2c2021.model

data class Speakers(val imageUrl:String="",
                    val name:String="",
                    val topic:String="",
                    val sessionUrl:String="",
                    val startUnix:Long=0,
                    val endUnix:Long=0)
