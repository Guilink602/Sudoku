package com.example.sudoku.viewModel
import android.widget.EditText

data class User(
    val nom:String?=null,
    val prenom:String?=null,
    val age: Int? =null,
    val score:Int?=null ,
    val nomdutilisateur:String,
    val uid:String?=null
){}
