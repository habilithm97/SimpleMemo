package com.example.simplememo.repository

import com.example.simplememo.room.Memo
import com.example.simplememo.room.MemoDao
import com.example.simplememo.viewmodel.SortOrder
import kotlinx.coroutines.flow.Flow

// Dao를 사용하여 DB 작업을 캡슐화
class MemoRepository(private val memoDao: MemoDao) {
    suspend fun addMemo(memo: Memo) {
        memoDao.addMemo(memo)
    }
    suspend fun deleteMemo(memo: Memo) {
        memoDao.deleteMemo(memo)
    }
    fun getAllSort(sortOrder: SortOrder): Flow<List<Memo>> {
        return when (sortOrder) {
            SortOrder.CREATE_DATE -> memoDao.getAllSortCreate()
            SortOrder.UPDATE_DATE -> memoDao.getAllSortUpdate()
        }
    }
}