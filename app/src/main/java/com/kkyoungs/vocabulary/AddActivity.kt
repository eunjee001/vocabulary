package com.kkyoungs.vocabulary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.kkyoungs.vocabulary.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var binding :ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        binding.addButton.setOnClickListener {
            add()
        }
    }


    private fun initView(){
        val types = listOf("명사", "동사","대명사", "형용사", "부사", "감탄사", "전치사", "접속사")

        binding.typeChipGroup.apply {
            types.forEach {
                createChip(it)
            }
        }
    }

    private fun createChip(text:String) :Chip{
        return Chip(this).apply {
            setText(text)
            isCheckable = true
            isClickable = true
        }
    }

    private fun add(){
        val text = binding.textInputEditText.text.toString()
        val mean = binding.meanTextInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).toString()
        val word = Word(text, mean, type)

        Thread{
            AppDatabase.getInstance(this)?.wordDao()?.insert(word)
            runOnUiThread{
                Toast.makeText(this, "저장을 완료 했습니다.", Toast.LENGTH_SHORT).show()

            }
            finish()

        }.start()
    }
}