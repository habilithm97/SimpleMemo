package com.example.simplememo.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.simplememo.R
import com.example.simplememo.databinding.ActivityMainBinding
import com.example.simplememo.ui.fragment.ListFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListFragment())
                .commit()
        }
        init()
    }

    private fun init() {
        binding.apply {
            setSupportActionBar(toolbar)
        }
    }

    override fun onSupportNavigateUp(): Boolean { // 툴바 뒤로가기 버튼 처리
        onBackPressed()
        return true // 직접 정의한 동작 수행
    }

    override fun onBackPressed() { // 백스택 관리
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            // 백스택에 프래그먼트가 없으면 기본 뒤로가기 동작 수행
            super.onBackPressedDispatcher.onBackPressed()
        }
    }

    // MemoFragment에서만 뒤로가기 버튼을 보이게 하기
    fun showBackButton(show: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.select -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}