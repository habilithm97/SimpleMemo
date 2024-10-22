package com.example.simplememo.ui.fragment

import android.content.Context
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        if(memoStr.isNotBlank()) {
            saveMemo(memoStr)
        }
    }

    private fun saveMemo(memoStr: String) {
        val dateFormat = SimpleDateFormat(getString(R.string.date_format), Locale.getDefault())
        val date = dateFormat.format(Date())

        val memo = Memo(memoStr, date)
        memoViewModel.addMemo(memo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBackButton(false)
        _binding = null // 메모리 릭 방지
    }
}