package com.kkyoungs.vocabulary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kkyoungs.vocabulary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter
    private var selectWord : Word?=null
    // 이렇게 하면 콜백 등록 가능
    private val updateAddWordResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        val isUpdate = result.data?.getBooleanExtra("isUpdate", false) ?: false

        if(result.resultCode == RESULT_OK && isUpdate){
            updateAddWord()
         }
    }

    private val updateEditWordResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result ->
        val editWord = result.data?.getParcelableExtra<Word>("editWord")

        if(result.resultCode == RESULT_OK && editWord != null){
            updateEditWord(editWord)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        binding.addButton.setOnClickListener {
            Intent(this, AddActivity::class.java).let{
              updateAddWordResult.launch(it)
            }
        }

        binding.deleteImageView.setOnClickListener {
            delete()
        }

        binding.editImageView.setOnClickListener {
            edit()
        }
    }

    private fun delete(){
        if (selectWord == null) return

        Thread{
            selectWord?.let {
                AppDatabase.getInstance(this)?.wordDao()?.delete(it)
                runOnUiThread {
                    wordAdapter.list.remove(it)
                    wordAdapter.notifyDataSetChanged()
                    binding.textTextView.text = ""
                    binding.meanTextView.text = ""
                    Toast.makeText(this, "삭제가 완료가 됬습니다.", Toast.LENGTH_SHORT).show()
                }
                selectWord = null
            }
        }.start()
    }

    private fun edit(){
        if (selectWord == null) return

        val intent = Intent(this, AddActivity::class.java).putExtra("originWord", selectWord)
        updateEditWordResult.launch(intent)
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

        Thread{
            val list = AppDatabase.getInstance(this)?.wordDao()?.getAll()?: emptyList()
            wordAdapter.list.addAll(list)
            runOnUiThread {
                wordAdapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun updateAddWord(){
        Thread{
            AppDatabase.getInstance(this)?.wordDao()?.getLatestWord()?.let{word->
                wordAdapter.list.add(0,word)
                runOnUiThread {
                    wordAdapter.notifyDataSetChanged()
                }
            }
        }.start()
    }

    private fun updateEditWord(word:Word){
       val index = wordAdapter.list.indexOfFirst {it.id == word.id}
            wordAdapter.list[index] = word
        runOnUiThread { wordAdapter.notifyDataSetChanged() }

    }
    override fun onClick(word: Word) {
        selectWord = word
        binding.textTextView.text = word.text
        binding.meanTextView.text = word.mean
        Toast.makeText(this, "${word.text}가 클릭 됐습니다.", Toast.LENGTH_SHORT).show()
    }
}