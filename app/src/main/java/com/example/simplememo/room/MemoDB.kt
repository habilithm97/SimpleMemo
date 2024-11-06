package com.example.simplememo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Memo::class], version = 4, exportSchema = false)
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
                    .addMigrations(MIGRATION_3_4)
                    .build()
                INSTANCE = instance // 생성된 인스턴스를 저장
                instance // 호출한 곳에 반환
            }
        }
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 새 테이블 생성
                database.execSQL("""create table memo_new (
                id integer primary key autoincrement not null,
                content text not null,
                createDate text not null,
                updateDate text not null)""")

                /*
                // 기존 데이터 복사 (date -> updateDate, createDate 기본값 0)
                database.execSQL("""insert into memo_new (id, content, updateDate, createDate)
                    select id, content, date as updateDate, 0 as createDate from memo""") */

                // 기존 테이블 삭제
                database.execSQL("drop table memo")

                // 새 테이블 이름을 기존 테이블 이름으로 변경
                database.execSQL("alter table memo_new rename to memo")
            }
        }
    }
}