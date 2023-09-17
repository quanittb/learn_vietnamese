package com.mobiai.app.model

data class User(var email:String,
var name:String,
var pass:String,
var urlImage:String?=null,
var ruby:Long = 0,
var totalXp :Long = 0)
