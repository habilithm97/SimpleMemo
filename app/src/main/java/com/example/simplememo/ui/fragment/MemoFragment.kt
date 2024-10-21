package com.example.simplememo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.simplememo.databinding.FragmentMemoBinding
import com.example.simplememo.ui.activity.MainActivity

class MemoFragment : Fragment() {
    private var _binding: FragmentMemoBinding? = null
    private val binding get() = _binding!!

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

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBackButton(false)
        _binding = null // 메모리 릭 방지
    }
}