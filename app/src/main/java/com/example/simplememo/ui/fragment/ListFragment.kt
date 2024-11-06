package com.example.simplememo.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplememo.R
import com.example.simplememo.adapter.MemoAdapter
import com.example.simplememo.databinding.FragmentListBinding
import com.example.simplememo.room.Memo
import com.example.simplememo.viewmodel.MemoViewModel
import com.example.simplememo.viewmodel.SortOrder

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val memoViewModel: MemoViewModel by viewModels()

    companion object {
        const val MEMO = "memo"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val memoAdapter = MemoAdapter(
            onItemClick = { memo -> // 클릭 리스너에서 memo를 전달 받음
                val memoFragment = MemoFragment().apply {
                    arguments = Bundle().apply { // MemoFragment에 전달할 데이터를 담은 번들 생성
                        putParcelable(MEMO, memo)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, memoFragment)
                    .addToBackStack(null)
                    .commit()
            }, onItemLongClick = { memo ->
                showDeleteDialog(memo) }
        )

        binding.apply {
            recyclerView.apply {
                adapter = memoAdapter
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    reverseLayout = true
                    stackFromEnd = true
                }
                setHasFixedSize(true) // 아이템 크기 고정 -> 성능 최적화
            }
            fab.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, MemoFragment())
                    .addToBackStack(null) // 백스택에 추가
                    .commit()
            }
            memoViewModel.getAll.observe(viewLifecycleOwner) { memoList ->
                memoAdapter.currentSortOrder = memoViewModel.sortOrder.value ?: SortOrder.UPDATE_DATE
                memoAdapter.submitList(memoList) {
                    val itemCount = memoAdapter.itemCount
                    if (itemCount > 0) {
                        recyclerView.smoothScrollToPosition(itemCount - 1) // 마지막 아이템 위치로 자동 스크롤
                    }
                }
            }
        }
    }

    private fun showDeleteDialog(memo: Memo) {
        AlertDialog.Builder(requireContext())
            .setTitle("삭제")
            .setMessage("선택한 메모를 삭제할까요 ?")
            .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                memoViewModel.deleteMemo(memo)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 릭 방지
    }
}