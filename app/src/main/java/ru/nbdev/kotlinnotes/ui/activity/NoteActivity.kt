package ru.nbdev.kotlinnotes.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_note.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.nbdev.kotlinnotes.R
import ru.nbdev.kotlinnotes.common.format
import ru.nbdev.kotlinnotes.common.getColorInt
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.ui.base.BaseActivity
import ru.nbdev.kotlinnotes.ui.model.NoteViewModel
import ru.nbdev.kotlinnotes.ui.model.NoteViewState
import java.util.*

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

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

    override val layoutRes = R.layout.activity_note
    override val model: NoteViewModel by viewModel()
    private var note: Note? = null
    private var color: Note.Color = Note.Color.WHITE
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
            model.loadNote(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?) = menuInflater.inflate(R.menu.note, menu).let { true }

    override fun renderData(data: Note?) {
        this.note = data
        supportActionBar?.title = note?.let { note ->
            note.lastChanged.format(DATE_TIME_FORMAT)
        } ?: getString(R.string.new_note_title)
        initView()
    }

    private fun initView() {
        note?.let { note ->
            edittext_title.setText(note.title)
            edittext_body.setText(note.text)
            color = note.color
        }

        toolbar.setBackgroundColor(color.getColorInt(this))

        edittext_title.addTextChangedListener(textChangeListener)
        edittext_body.addTextChangedListener(textChangeListener)

        color_picker.onColorClickListener = { color ->
            this.color = color
            toolbar.setBackgroundColor(color.getColorInt(this))
            saveNote()
        }
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
                color = this.color,
                lastChanged = Date()
        ) ?: createNewNote()

        note?.let {
            model.save(it)
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
        R.id.palette -> {
            togglePalette()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun togglePalette() {
        if (color_picker.isOpen) {
            color_picker.close()
        } else {
            color_picker.open()
        }
    }
}
