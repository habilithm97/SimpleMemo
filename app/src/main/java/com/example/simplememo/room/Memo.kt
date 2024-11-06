package com.example.simplememo.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize // Parcelable 인터페이스 자동 구현
@Entity(tableName = "memo")
data class Memo(
    val content: String,
    val createDate: String,
    val updateDate: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable

/**
 * Parcelable
-객체를 직렬화하여 데이터를 전달할 수 있게 하는 인터페이스
-직렬화 : 객체의 상태를 저장하거나 전송 가능한 형태로 변환하는 과정
-주로 액티비티 간, 프래그먼트 간의 데이터 전달에 사용됨

 * Parcel
-객체의 데이터를 저장하거나 전송할 수 있는 일종의 컨테이너
-객체의 데이터를 쓰기/읽기 작업을 통해 직렬화/역직렬화
 */