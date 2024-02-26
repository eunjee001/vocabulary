package com.kkyoungs.vocabulary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.chip.Chip
import com.kkyoungs.vocabulary.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var binding :ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
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
}