package com.example.simplememo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.simplememo.repository.MemoRepository
import com.example.simplememo.room.Memo
import com.example.simplememo.room.MemoDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Repository를 통해 DB와 상호작용, UI에 필요한 데이터를 제공
class MemoViewModel(application: Application) : AndroidViewModel(application) {
    private val memoRepository: MemoRepository
    val getAll: LiveData<List<Memo>>

    init {
        val memoDao = MemoDB.getInstance(application).memoDao()
        memoRepository = MemoRepository(memoDao)
        getAll = memoRepository.getAll.asLiveData()
    }

    fun addMemo(memo: Memo) {
        // 백그라운드에서 추가 (입출력 작업은 IO 스레드에서 처리)
        viewModelScope.launch(Dispatchers.IO) {
            memoRepository.addMemo(memo)
        }
    }

    fun updateMemo(memo: Memo) {
        viewModelScope.launch(Dispatchers.IO) {
            // 메모 삭제 후 다시 추가 -> 메모 수정 시 리스트 마지막 위치로 메모 이동
            memoRepository.deleteMemo(memo) // 삭제 -> 리스트 순서 조정
            memoRepository.addMemo(memo.copy(id = 0)) // id = 0 -> 새로운 항목으로 처리
        }
    }

    fun deleteMemo(memo: Memo) {
        viewModelScope.launch(Dispatchers.IO) {
            memoRepository.deleteMemo(memo)
        }
    }
}
/**
 * viewModelScope
  -ViewModel에서 코루틴을 관리하기 위한 스코프
  -ViewModel이 사라질 때 함께 취소되므로, 메모리 릭 없는 안전한 비동기 작업 수행 가능
 */