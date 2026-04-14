package com.IT487.taskmaster

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DeleteConfirmDialogFragment(private val onConfirm: (Boolean) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to permanently delete this task?")
            .setPositiveButton("Yes") { _, _ -> onConfirm(true) }
            .setNegativeButton("Cancel") { _, _ -> onConfirm(false) }
            .create()
    }
}