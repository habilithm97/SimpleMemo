package com.example.simplememo.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class Memo(val content: String, val date: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}
