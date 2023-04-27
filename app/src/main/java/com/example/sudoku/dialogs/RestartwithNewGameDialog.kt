package com.example.sudoku.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.sudoku.R
import com.example.sudoku.view.SudokuGridView

class RestartwithNewGameDialog :DialogFragment(){

    private var restartGameListener: NoticeRestartDialogListener? = null

    interface NoticeRestartDialogListener {
        fun onDialogRestartGameClick(dialog: DialogFragment)
        fun onDialogCancelRestartClick(dialog: DialogFragment)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.restart_with_new_game_dialog, null))
                // Add action buttons
                .setPositiveButton("Restart",
                    DialogInterface.OnClickListener { dialog, id ->
                        restartGameListener?.onDialogRestartGameClick(this)
                        // sign in the user ...
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        restartGameListener?.onDialogCancelRestartClick(this)
                    })
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")

    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context:Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            restartGameListener = context as NoticeRestartDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }

}