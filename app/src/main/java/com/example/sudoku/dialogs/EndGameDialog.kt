package com.example.sudoku.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.sudoku.R
import com.example.sudoku.databinding.EndGameDialogBinding

class EndGameDialog:DialogFragment() {
    private var EndGameListener: NoticeEndGameDialogListener? = null
    lateinit var endGameDialogBinding:EndGameDialogBinding

    interface NoticeEndGameDialogListener {
        fun onDialogEndGameClick(dialog: DialogFragment)
        fun onShareClick(dialog:DialogFragment)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            endGameDialogBinding=EndGameDialogBinding.inflate(this.layoutInflater)
            builder.setView(inflater.inflate(R.layout.end_game_dialog, null))
                // Add action buttons
                .setPositiveButton("Restart",
                    DialogInterface.OnClickListener { dialog, id ->
                        EndGameListener?.onDialogEndGameClick(this)
                        // sign in the user ...
                    })
            endGameDialogBinding.ShareBtn.setOnClickListener{EndGameListener?.onShareClick(this)}
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            EndGameListener = context as NoticeEndGameDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }

}