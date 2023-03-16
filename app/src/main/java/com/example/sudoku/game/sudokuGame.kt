package com.example.sudoku.game

import androidx.lifecycle.MutableLiveData

class sudokuGame {
    var selectedCellLiveData=MutableLiveData<Pair<Int,Int>>()
    var cellsLiveData=MutableLiveData<List<Cell>>()
    private  var selectedRow=-1
    private var selectedCol=-1
    var cells :List<Cell> = emptyList()
    var nbMistake=0;

    init {
        val cellsTemp=List(9*9){i->Cell(i/9,i%9,  (i%9)+1, false,mutableSetOf<Int>(),i)}
        cells=cellsTemp
        cells[0].value=0
        cells[0].notes= mutableSetOf(1,2,3,4,5,6,7,8,9)
        cells[11].isStartingCell=true
        cells[21].isStartingCell=true
        selectedCellLiveData.postValue(Pair(selectedRow,selectedCol))
        cellsLiveData.postValue(cells)
    }

    fun handleInput(number:Int,isTakingNotes:Boolean) {
        if (selectedRow == -1 || selectedCol == -1) return
        val cell = getCells(selectedRow, selectedCol)
        if (cell.isStartingCell) return

        if (isTakingNotes) {
            if (cell.notes.contains(number))
                cell.notes.remove(number)
            else
                cell.notes.add(number)
        } else {
            cell.value = number
        }
        cellsLiveData.postValue(cells)
    }

    fun updateCell(row:Int,col:Int){
        if(!getCells(row,col).isStartingCell){
        selectedRow=row
        selectedCol=col
        selectedCellLiveData.postValue(Pair(row,col))}
    }

    fun getCells(row:Int,col:Int)= cells[row*9+col]

    fun delete(isTakingNotes: Boolean){
        if (selectedRow == -1 || selectedCol == -1) return
        val cell=getCells(selectedRow,selectedCol)
        if(isTakingNotes)
            cell.notes.clear()
        else{
           cell.value=0
        }
        cellsLiveData.postValue(cells)
    }
}