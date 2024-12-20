package com.example.simplememo.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplememo.R
import com.example.simplememo.databinding.ItemMemoBinding
import com.example.simplememo.room.Memo
import com.example.simplememo.viewmodel.SortOrder

class MemoAdapter(private val onItemClick: (Memo) -> Unit,
                  private val onItemLongClick: (Memo) -> Unit
) : ListAdapter<Memo, MemoAdapter.MemoViewHolder>(DIFF_CALLBACK) {

    var currentSortOrder: SortOrder = SortOrder.UPDATE_DATE
        set(value) {
            field = value
            // currentSortOrder가 변경될 때마다 데이터 갱신
            submitList(currentList) // 현재 리스트를 submit하여 새로 갱신
        }

    inner class MemoViewHolder(private val binding: ItemMemoBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(memo: Memo) {
                binding.apply {
                    this.memo = memo // XML에서 직접 데이터를 참조하여 자동으로 UI 업데이트

                    // SortOrder에 따라 표시하는 데이터 조정
                    tvDate.text = when (currentSortOrder) {
                        SortOrder.CREATE_DATE -> {
                            tvDate.setTextColor(Color.BLACK)
                            memo.createDate
                        }
                        SortOrder.UPDATE_DATE -> {
                            tvDate.setTextColor(Color.BLUE)
                            memo.updateDate
                        }
                    }
                    executePendingBindings() // 지연된 바인딩을 즉시 실행하여 데이터가 뷰에 반영되도록 함

                    root.setOnClickListener {
                        onItemClick(memo)
                    }
                    root.setOnLongClickListener {
                        showPopupMenu(it, memo)
                        true
                    }
                }
            }

        private fun showPopupMenu(view: View, memo: Memo) {
            val popup = PopupMenu(view.context, view)
            popup.menuInflater.inflate(R.menu.memo_context_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.delete -> {
                        onItemLongClick(memo)
                          true
                    } else -> false
                }
            }
            popup.show()
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Memo>() {
            override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val binding = ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}