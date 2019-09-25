package ru.nbdev.kotlingb.ui.model

import android.arch.lifecycle.ViewModel
import android.util.Log
import ru.nbdev.kotlingb.data.NotesRepository
import ru.nbdev.kotlingb.data.entity.Note

class NoteViewModel : ViewModel() {

    private var pendingNote: Note? = null

    fun save(note: Note) {
        Log.d(this::class.java.name, "pendingNote SAVE !!!!!!!")
        pendingNote = note
    }

    override fun onCleared() {
        Log.d(this::class.java.name, "onCleared() !!!!!!!")
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
    }
}