package ru.nbdev.kotlinnotes.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import ru.nbdev.kotlinnotes.R

class LogoutDialog : DialogFragment() {

    companion object {
        val TAG = LogoutDialog::class.java.name + "TAG"
        fun createInstance() = LogoutDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(context!!)
                    .setTitle(R.string.logout_dialog_exit)
                    .setMessage(R.string.are_you_shure)
                    .setPositiveButton(R.string.yes) { _, _ -> (activity as LogoutListener).onLogout() }
                    .setNegativeButton(R.string.no) { _, _ -> dismiss() }
                    .create()


    interface LogoutListener {
        fun onLogout()
    }
}