package ru.nbdev.kotlinnotes.ui.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.nbdev.kotlinnotes.R
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.ui.adapter.NotesRecyclerAdapter
import ru.nbdev.kotlinnotes.ui.base.BaseActivity
import ru.nbdev.kotlinnotes.ui.model.MainViewModel
import ru.nbdev.kotlinnotes.ui.model.MainViewState


class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    override val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override val layoutRes = R.layout.activity_main
    lateinit var adapter: NotesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
}
