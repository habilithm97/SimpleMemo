package com.example.simplememo.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.example.simplememo.R
import com.example.simplememo.databinding.FragmentMemoBinding
import com.example.simplememo.room.Memo
import com.example.simplememo.ui.activity.MainActivity
import com.example.simplememo.viewmodel.MemoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MemoFragment : Fragment() {
    private var _binding: FragmentMemoBinding? = null
    private val binding get() = _binding!!
    private val memoViewModel: MemoViewModel by viewModels()
    private var memo: Memo? = null // 전달 받은 메모
    private var prevMemo: String? = null // 수정 전 메모

    companion object {
        const val MEMO = "memo"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memo = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33 이상
            arguments?.getParcelable(MEMO, Memo::class.java) // 타입 안전한 방식으로 가져옴
        } else {
            arguments?.getParcelable(MEMO)
        }
        memo?.let { // memo가 있으면 수정 모드
            binding.edtMemo.setText(it.content)
            prevMemo = it.content
        }

        // MainActivity 툴바에 뒤로가기 버튼 활성화
        (activity as? MainActivity)?.showBackButton(true)
    }

    override fun onResume() {
        super.onResume()

        binding.edtMemo.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.edtMemo, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onPause() {
        super.onPause()

        val memoStr = binding.edtMemo.text.toString()

        if (memoStr.isNotBlank() && memoStr != prevMemo) {
            memo?.let { // memo가 있으면 수정 모드
                updateMemo(memoStr)
            } ?: run { // memo가 없으면 추가 모드
                saveMemo(memoStr)
            }
        }
    }

    private fun saveMemo(memoStr: String) {
        val dateFormat = SimpleDateFormat(getString(R.string.date_format), Locale.getDefault())
        val date = dateFormat.format(Date())

        val memo = Memo(memoStr, createDate = date, updateDate = date)
        memoViewModel.addMemo(memo)
    }

    private fun updateMemo(memoStr: String) {
        val dateFormat = SimpleDateFormat(getString(R.string.date_format), Locale.getDefault())
        val date = dateFormat.format(Date())

        val updatedMemo = memo?.copy(content = memoStr, updateDate = date) // 내용만 수정된 메모 생성
        updatedMemo?.let {
            memoViewModel.updateMemo(it) // ViewModel에서 메모 업데이트
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBackButton(false)
        _binding = null // 메모리 릭 방지
    }
}