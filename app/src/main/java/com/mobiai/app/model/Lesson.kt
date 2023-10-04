package com.mobiai.app.model

import android.content.Context
import com.mobiai.R

data class Lesson(val image:Int,val title:String,val numberDone:Long){
    companion object {
        fun lessonList(context: Context): List<Lesson> {
            return listOf(
                Lesson(R.drawable.img_part1, "Part 1: Entering the journey", 3),
                Lesson(R.drawable.img_part1, "Part 1: Entering the journey", 2),
                Lesson(R.drawable.img_part1, "Part 1: Entering the journey", 1),
                Lesson(R.drawable.img_part1, "Part 1: Entering the journey", 0),
                Lesson(R.drawable.img_part1, "Part 1: Entering the journey", 4),
                Lesson(R.drawable.img_part1, "Part 1: Entering the journey", 5),
            )
        }
    }
}
