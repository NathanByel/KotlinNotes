package ru.nbdev.kotlinnotes.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.koin.android.viewmodel.ext.android.viewModel
import ru.nbdev.kotlinnotes.R
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.ui.adapter.NotesRecyclerAdapter
import ru.nbdev.kotlinnotes.ui.base.BaseActivity
import ru.nbdev.kotlinnotes.ui.model.MainViewModel
import ru.nbdev.kotlinnotes.ui.model.MainViewState


class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override val model: MainViewModel by viewModel()
    override val layoutRes = R.layout.activity_main
    private lateinit var adapter: NotesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        recyclerInit()

        fab.setOnClickListener {
            NoteActivity.start(this)
        }
    }

    private fun recyclerInit() {
        recycler_main_notes.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesRecyclerAdapter({
            NoteActivity.start(this, it)
        }, {
            removeNote(it)
            true
        })

        recycler_main_notes.adapter = adapter
    }

    private fun removeNote(note: Note) {
        alert {
            titleResource = R.string.remove_note_dialog_title
            positiveButton(R.string.remove_note_dialog_positive) { model.removeNote(note) }
            negativeButton(R.string.remove_note_dialog_negative) { dialog -> dialog.dismiss() }
        }.show()
    }

    override fun renderData(data: List<Note>?) {
        data?.let {
            adapter.notes = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = MenuInflater(this).inflate(R.menu.main, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout -> showLogoutDialog().let { true }
        else -> false
    }

    private fun showLogoutDialog() {
        alert {
            titleResource = R.string.logout_dialog_title
            messageResource = R.string.logout_dialog_message
            positiveButton(R.string.logout_dialog_positive) { logout() }
            negativeButton(R.string.logout_dialog_negative) { dialog -> dialog.dismiss() }
        }.show()
    }

    private fun logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
    }
}
