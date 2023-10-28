package com.mobiai.base.basecode.extensions

import android.util.Log

fun Any.LogD(string: String){
    Log.d("abcd : ${this::class.java.simpleName}","$string")
}