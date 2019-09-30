package ru.nbdev.kotlinnotes.ui.activity

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import ru.nbdev.kotlinnotes.R
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.ui.adapter.NotesRecyclerAdapter
import ru.nbdev.kotlinnotes.ui.base.BaseActivity
import ru.nbdev.kotlinnotes.ui.fragments.LogoutDialog
import ru.nbdev.kotlinnotes.ui.model.MainViewModel
import ru.nbdev.kotlinnotes.ui.model.MainViewState


class MainActivity : BaseActivity<List<Note>?, MainViewState>(), LogoutDialog.LogoutListener {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override val layoutRes = R.layout.activity_main
    lateinit var adapter: NotesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        recyclerInit()

        main_fab.setOnClickListener {
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
        val dialog = AlertDialog.Builder(this).apply {
            setTitle(R.string.remove_note)
            //setMessage("")
            setCancelable(true)

            setPositiveButton(R.string.remove) { dialog, _ ->
                viewModel.remove(note)
                dialog.dismiss()
            }

            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }

        }.create()

        dialog.show()
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
        supportFragmentManager.findFragmentByTag(LogoutDialog.TAG)
                ?: LogoutDialog.createInstance().show(supportFragmentManager, LogoutDialog.TAG)
    }

    override fun onLogout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
    }
}
