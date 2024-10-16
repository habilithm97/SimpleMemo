package com.example.simplememo.repository

import com.example.simplememo.room.Memo
import com.example.simplememo.room.MemoDao
import kotlinx.coroutines.flow.Flow

// Dao를 사용하여 DB 작업을 캡슐화
class MemoRepository(private val memoDao: MemoDao) {
    suspend fun addMemo(memo: Memo) {
        memoDao.addMemo(memo)
    }
    val getAll: Flow<List<Memo>> = memoDao.getAll()
}