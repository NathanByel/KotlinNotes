package ru.nbdev.kotlinnotes.ui.model

import android.arch.lifecycle.Observer
import android.support.annotation.VisibleForTesting
import ru.nbdev.kotlinnotes.OpenClassOnDebug
import ru.nbdev.kotlinnotes.data.NotesRepository
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.data.model.NoteResult
import ru.nbdev.kotlinnotes.ui.base.BaseViewModel

@OpenClassOnDebug
class NoteViewModel(private val notesRepository: NotesRepository) : BaseViewModel<Note?, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private val pendingNote: Note?
        get() = viewStateLiveData.value?.data

    fun save(note: Note) {
        viewStateLiveData.value = NoteViewState(note = note)
    }

    fun loadNote(noteId: String) {
        notesRepository.getNoteById(noteId).observeForever(Observer<NoteResult> { noteResult ->
            if (noteResult == null) return@Observer

            when (noteResult) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(note = noteResult.data as? Note)
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = noteResult.error)
            }
        })
    }

    @VisibleForTesting
    public override fun onCleared() {
        pendingNote?.let {
            notesRepository.saveNote(it)
        }
    }
}