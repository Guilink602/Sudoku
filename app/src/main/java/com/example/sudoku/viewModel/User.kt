package com.example.sudoku.viewModel
import android.widget.EditText

data class User(
    val nom: String,
    val prenom: String,
    val score: Int?= null,
    val nomdutilisateur: String = "",
    val uid: String = ""
) {
    constructor() : this("", "", null, "", "")
}
