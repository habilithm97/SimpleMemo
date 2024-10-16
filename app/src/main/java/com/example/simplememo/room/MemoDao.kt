package com.example.simplememo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMemo(memo: Memo)

    @Query("select * from memo order by id")
    fun getAll(): Flow<List<Memo>>
}