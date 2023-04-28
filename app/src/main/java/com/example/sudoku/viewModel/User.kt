package com.example.sudoku.viewModel
import android.widget.EditText

data class User(
    val nom: String,
    val prenom: String,
    val temps: String? = null,
    val erreurs: Int? = null,
    val score: Int?= null,
    val nomdutilisateur: String = "",
    val uid: String = "",
    var imageUrl:String=""
) {
    constructor() : this("", "", null, null, null,"", "")
}
