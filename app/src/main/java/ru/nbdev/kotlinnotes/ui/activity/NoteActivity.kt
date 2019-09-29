package ru.nbdev.kotlinnotes.ui.activity

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_note.*
import ru.nbdev.kotlinnotes.R
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.ui.base.BaseActivity
import ru.nbdev.kotlinnotes.ui.model.NoteViewModel
import ru.nbdev.kotlinnotes.ui.model.NoteViewState
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    override val layoutRes = R.layout.activity_note

    override val viewModel: NoteViewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + ".extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"
        private const val SAVE_DELAY = 2000L

        fun start(context: Context, note: Note? = null) {
            val intent = Intent(context, NoteActivity::class.java).apply {
                note?.let {
                    putExtra(EXTRA_NOTE, it.id)
                }
            }
            context.startActivity(intent)
        }
    }

    private var note: Note? = null
    private var autoSaveHandler: Handler = Handler()

    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            noteChanged()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let {
            viewModel.loadNote(it)
        }
    }

    override fun renderData(data: Note?) {
        this.note = data
        supportActionBar?.title = note?.let {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note!!.lastChanged)
        } ?: getString(R.string.new_note_title)

        initView()
    }

    private fun initView() {

        note?.let {
            edittext_title.setText(it.title)
            edittext_body.setText(it.text)
            toolbar.setBackgroundColor(ContextCompat.getColor(this, it.color.resColor))
        }

        edittext_title.addTextChangedListener(textChangeListener)
        edittext_body.addTextChangedListener(textChangeListener)
    }


    private fun noteChanged() {
        autoSaveHandler.removeCallbacksAndMessages(null)
        autoSaveHandler.postDelayed({
            saveNote()
        }, SAVE_DELAY)
    }

    private fun saveNote() {
        if (edittext_title.text == null || (edittext_title.text?.length ?: 0) < 3)
            return

        note = note?.copy(
                title = edittext_title.text.toString(),
                text = edittext_body.text.toString(),
                lastChanged = Date()
        ) ?: createNewNote()

        note?.let {
            viewModel.save(it)
        }
    }

    private fun createNewNote() = Note(
            UUID.randomUUID().toString(),
            edittext_title.text.toString(),
            edittext_body.text.toString()
    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            autoSaveHandler.removeCallbacksAndMessages(null)
            saveNote()
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
