package com.example.sudoku

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sudoku.database.Database
import com.example.sudoku.databinding.ActivityMainBinding
import com.example.sudoku.dialogs.EndGameDialog
import com.example.sudoku.dialogs.RestartwithNewGameDialog
import com.example.sudoku.game.Cell
import com.example.sudoku.game.Grid
import com.example.sudoku.game.sudokuGame
import com.example.sudoku.view.SudokuGridView
import com.example.sudoku.viewModel.SudokuViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(),SudokuGridView.OnTouchListener,
    RestartwithNewGameDialog.NoticeRestartDialogListener,
    EndGameDialog.NoticeEndGameDialogListener {

    private lateinit var viewModel:SudokuViewModel
    private lateinit var binding: ActivityMainBinding
    lateinit var dbSudoku: Database
    lateinit var dbFirebase: DatabaseReference

    //  private  val buttons= listOf(binding.OneButton,binding.TwoButton,binding.ThreeButton,binding.FourButton,binding.FiveButton,binding.SixButton,binding.SevenButton,binding.EightButton,binding.NineButton)

    override fun onCreate(savedInstanceState: Bundle?) {

        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setContentView(R.layout.activity_main)
        binding.Board.registerListener(this)

        var Mistake = binding.TextViewMistake

        viewModel= ViewModelProvider(this).get(SudokuViewModel::class.java)
        viewModel.sudokuGame.selectedCellLiveData.observe(this, Observer {
            updateSelectedCell(it)
            Mistake.setText("Mistakes: ${viewModel.sudokuGame.nbMistake}")
        })
        viewModel.sudokuGame.gridLiveData.observe(this,Observer{updateCell(it)})

        viewModel.sudokuGame.timer = binding.SimpleChronometer

        val buttons= listOf(binding.OneButton,binding.TwoButton,binding.ThreeButton,binding.FourButton,binding.FiveButton,binding.SixButton,binding.SevenButton,binding.EightButton,binding.NineButton)

        buttons.forEachIndexed{index,button->button.setOnClickListener{viewModel.sudokuGame.handleInput(index+1,binding.NoteSwitch.isChecked)
            GameOver()}}
        binding.Delete.setOnClickListener{viewModel.sudokuGame.delete(binding.NoteSwitch.isChecked)}
        binding.NewGame.setOnClickListener{AskStartNewGame()}
        binding.Retour.setOnClickListener{BackToMenu()}
        viewModel.sudokuGame.startTimer()
    }

    private fun updateCell(grid:Grid?)=grid?.let{
        binding.Board.updateCell(grid)
        //Check if game is over
    }


    private fun updateSelectedCell(cell:Pair<Int,Int>?)=cell?.let{
        binding.Board.updateSelectedCell(cell.first,cell.second)
    }

    override  fun onCellTouch(row:Int,col:Int){
        viewModel.sudokuGame.updateCell(row,col)
    }

    private fun saveResultToLocalDatabase(time: String, errors: Int, score: Int) {
        dbSudoku = Database(this, null)
        val currentUser = FirebaseAuth.getInstance().currentUser
        dbSudoku.addData(currentUser!!.uid, time, errors, score)
    }

    private fun saveResultToFirebaseDatabase(time: String, errors: Int, score: Int) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser!!.uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var ancienScore = dataSnapshot.child("score").getValue(Int::class.java)

                if (ancienScore != null) {
                    if(ancienScore < score) {
                        dbFirebase.child("Users").child("score").setValue(score)
                        dbFirebase.child("Users").child("temps").setValue(time)
                        dbFirebase.child("Users").child("erreurs").setValue(errors)
                    }
                }
                else {
                    dbFirebase.child("Users").child("score").setValue(score)
                    dbFirebase.child("Users").child("temps").setValue(time)
                    dbFirebase.child("Users").child("erreurs").setValue(errors)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun GameOver() {
        if (viewModel.sudokuGame.grid.gridCompleted()) {

            viewModel.sudokuGame.stopTimer()

            var score = 10000
            val penaliteErreur = (viewModel.sudokuGame.nbMistake * 100)
            val temps = SystemClock.elapsedRealtime() - viewModel.sudokuGame.timer!!.getBase()
            val penaliteTemps = temps/100
            score = (score - (penaliteErreur + penaliteTemps)).toInt()

            saveResultToLocalDatabase(viewModel.sudokuGame.timer?.text.toString(), viewModel.sudokuGame.nbMistake, score)

            saveResultToFirebaseDatabase(viewModel.sudokuGame.timer?.text.toString(), viewModel.sudokuGame.nbMistake, score)
            // Implement popup and add data to database
            var dialog = EndGameDialog()
            dialog.show(supportFragmentManager, "customDialog")
        }
    }


    fun AskStartNewGame(){
        var dialog=RestartwithNewGameDialog()
        dialog.show(supportFragmentManager,"customDialog")
    }


    override fun onDialogRestartGameClick(dialog: DialogFragment) {
        viewModel.sudokuGame.StartNewGame()
    }

    override fun onDialogCancelRestartClick(dialog: DialogFragment) {
        // User touched the dialog's negative button
    }

    override fun onDialogEndGameClick(dialog: DialogFragment) {
        viewModel.sudokuGame.StartNewGame()
    }

    override fun onShareClick(dialog: DialogFragment) {
        // Récupérer le temps et le nombre d'erreurs du jeu actuel
        val gameTime = viewModel.sudokuGame.timer?.text.toString()
        val gameErrors = viewModel.sudokuGame.nbMistake

        // Créer le texte à partager
        val shareText = "J'ai terminé un jeu de Sudoku en $gameTime avec $gameErrors erreurs. Qui peut faire mieux que moi ?"

        // Créer un Intent pour partager le texte
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        // Lancer le partage via le sélecteur d'intentions
        startActivity(Intent.createChooser(shareIntent, "Partager mon résultat via :").apply {
            putExtra(Intent.EXTRA_EXCLUDE_COMPONENTS, arrayOf(
                    ComponentName(
                        "com.android.bluetooth",
                        "com.android.bluetooth.opp.BluetoothOppLauncherActivity"
                    ),
                    ComponentName(
                        "com.google.android.apps.docs",
                        "com.google.android.apps.docs.app.SendTextToClipboardActivity"
                    )
                )
            )
        })
    }


    fun BackToMenu(){
        super.finish()
    }

}

