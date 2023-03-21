package com.example.sudoku.game

class Grid(val size:Int) {

    var cells :List<Cell> = emptyList()
    var availableInt= intArrayOf(1,2,3,4,5,6,7,8,9)

    var path:ArrayList<Pair<Int,Pair<Int,Int>>> = ArrayList()

    fun generateGrid(){
        cells=List(9*9){i->Cell(i/9,i%9,  0, false,mutableSetOf<Int>(),i)}
        generateCompleteSoluce()
    }

    fun getCells(row:Int,col:Int)= cells[row*9+col]




    fun generateCompleteSoluce():Boolean{
       var isValid:Boolean=false
        if(gridNotEmpty()){
            isValid=true
        }
        else {
            EndSearch@ for (r in 0 until size) {
                for (c in 0 until size) {
                    if (getCells(r, c).value == 0) {
                        availableInt.shuffle()
                        for (i: Int in availableInt) {
                            if (validatePosition(r, c, i)) {
                                getCells(r, c).value = i
                                //No empty cell

                                    if (generateCompleteSoluce())
                                        return true
                                }
                            }
                        getCells(r, c).value = 0
                        return false
                        }

                    }
                }
            }

        return isValid

        /*
        for(r in 0 until size)
        {
            for(c in 0 until size)
            {
                 if(getCells(r,c).value==0){
                    availableInt.shuffle()
                    for(i:Int in availableInt){
                        if(validatePosition(r,c,i))
                        {
                            getCells(r,c).value=i
                            //No empty cell
                            if(gridNotEmpty())
                                return true
                            else{
                                if(generateCompleteSoluce())
                                    return true
                                else{
                                    getCells(r,c).value=0
                                    return false
                                }
                            }
                        }
                    }
                 }

            }
        }*/
    return false
    }

    fun validatePosition(row:Int,col: Int,value:Int):Boolean {
        cells.forEach {

            val r = it.row
            val c = it.col


            if (r == row || c == col) {
                if (getCells(r, c).value == value)
                     return false
            } else if (r / 3 == row / 3 && c / 3 == col / 3) {
                if (getCells(r, c).value == value)
                    return false
            }

        }
        return true
    }

    fun validateColPosition(row:Int,col: Int,value:Int):Boolean{
        for(r in 0 until 9)
        {
            if(r!=row)
            {
                if(getCells(r,col).value==value)
                    return false
            }
        }
        return true
    }

    fun validateRowPosition(row:Int,col: Int,value:Int):Boolean{
        for(c in 0 until 9)
        {
            if(c!=col)
            {
                if(getCells(row,c).value==value)
                    return false
            }
        }
        return true
    }

    fun validateCasePosition(row:Int,col: Int,value:Int):Boolean{


        return  true
    }


    fun gridNotEmpty():Boolean{
        cells.forEach {
            if(it.value==0)
                return false
        }
        return true
    }
}