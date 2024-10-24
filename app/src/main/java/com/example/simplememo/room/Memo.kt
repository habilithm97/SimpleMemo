package com.example.simplememo.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Parcelize // Parcelable 인터페이스 자동 구현
@Entity(tableName = "memo")
data class Memo(val content: String, val date: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0) : Parcelable {

        // Parcel로부터 데이터를 읽어옴
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    )

    // Parcel에 데이터를 기록
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
        parcel.writeString(date)
        parcel.writeInt(id)
    }

    // Parcelable의 내용을 설명
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Memo> {
        // Parcel로부터 Memo 객체 생성
        override fun createFromParcel(parcel: Parcel): Memo {
            return Memo(parcel)
        }
        // Memo 배열 생성
        override fun newArray(size: Int): Array<Memo?> {
            return arrayOfNulls(size)
        }
    }
}
annotation class Parcelize

/**
 * Parcelable
  -객체를 직렬화하여 데이터를 전달할 수 있게 하는 인터페이스
  -직렬화 : 객체의 상태를 저장하거나 전송 가능한 형태로 변환하는 과정
  -주로 액티비티 간, 프래그먼트 간의 데이터 전달에 사용됨

 * Parcel
  -객체의 데이터를 저장하거나 전송할 수 있는 일종의 컨테이너
  -객체의 데이터를 쓰기/읽기 작업을 통해 직렬화/역직렬화
 */