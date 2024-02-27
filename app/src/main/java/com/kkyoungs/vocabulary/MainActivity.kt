package com.kkyoungs.vocabulary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kkyoungs.vocabulary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
    }

    private fun initRecyclerView(){
        wordAdapter = WordAdapter(mutableListOf(), this)
        binding.wordRecyclerView.apply {
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

            /**
             * ItemDecoration을 사용하면 애플리케이션이 어댑터 데이터 세트의 특정 항목 보기에 특수 도면 및 레이아웃 오프셋을 추가할 수 있습니다.
             * 이는 항목, 하이라이트, 시각적 그룹 경계 등 사이에 구분 선을 그리는 데 유용할 수 있습니다.
             */
            val dividerItemDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)

        }
    }

    override fun onClick(word: Word) {
        Toast.makeText(this, "${word.text}가 클릭 됐습니다.", Toast.LENGTH_SHORT).show()
    }
}