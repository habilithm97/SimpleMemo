package com.example.simplememo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Memo::class], version = 1, exportSchema = false)
abstract class MemoDB : RoomDatabase() {
    abstract fun memoDao(): MemoDao

    companion object {
        @Volatile // 인스턴스가 여러 스레드에서 동시에 접근되더라도 일관된 값을 읽을 수 있도록 보장
        private var INSTANCE: MemoDB? = null
        private const val DB_NAME = "memo_db"

        fun getInstance(context: Context): MemoDB {
            return INSTANCE ?: synchronized(this) { // 동기화 -> 멀티 스레드 환경에서 인스턴스를 안전하게 생성
                val instance = Room.databaseBuilder(
                    context.applicationContext, MemoDB::class.java, DB_NAME)
                    .build()
                INSTANCE = instance // 생성된 인스턴스를 저장
                instance // 호출한 곳에 반환
            }
        }
    }
}