package com.mobiai.app.model

data class Question(val questionCode : String , val content : String, val topicCode : String, val level : Int,
                    val option1 : String,val option2 : String,val option3 : String,val option4 : String, val answer : String,
                    val urlImg : String,val typeCode : Int)