package com.example.sudoku

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sudoku.databinding.ActivityMainBinding
import com.example.sudoku.game.Cell
import com.example.sudoku.game.Grid
import com.example.sudoku.view.SudokuGridView
import com.example.sudoku.viewModel.SudokuViewModel
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity(),SudokuGridView.OnTouchListener{

    private lateinit var viewModel:SudokuViewModel
    private lateinit var binding: ActivityMainBinding

  //  private  val buttons= listOf(binding.OneButton,binding.TwoButton,binding.ThreeButton,binding.FourButton,binding.FiveButton,binding.SixButton,binding.SevenButton,binding.EightButton,binding.NineButton)

    override fun onCreate(savedInstanceState: Bundle?) {

        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // setContentView(R.layout.activity_main)
       binding.Board.registerListener(this)

        viewModel= ViewModelProvider(this).get(SudokuViewModel::class.java)
        viewModel.sudokuGame.selectedCellLiveData.observe(this, Observer { updateSelectedCell(it) })
        viewModel.sudokuGame.gridLiveData.observe(this,Observer{updateCell(it)})
        val buttons= listOf(binding.OneButton,binding.TwoButton,binding.ThreeButton,binding.FourButton,binding.FiveButton,binding.SixButton,binding.SevenButton,binding.EightButton,binding.NineButton)

        buttons.forEachIndexed{index,button->button.setOnClickListener{viewModel.sudokuGame.handleInput(index+1,binding.NoteSwitch.isChecked)}}
        binding.Delete.setOnClickListener{viewModel.sudokuGame.delete(binding.NoteSwitch.isChecked)}
    }

    private fun updateCell(grid:Grid?)=grid?.let{
        binding.Board.updateCell(grid)
    }


    private fun updateSelectedCell(cell:Pair<Int,Int>?)=cell?.let{
      binding.Board.updateSelectedCell(cell.first,cell.second)
    }

  override  fun onCellTouch(row:Int,col:Int){
        viewModel.sudokuGame.updateCell(row,col)
    }
}