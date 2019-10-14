package ru.nbdev.kotlinnotes.ui.model

import android.arch.lifecycle.Observer
import ru.nbdev.kotlinnotes.data.NotesRepository
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.data.model.NoteResult
import ru.nbdev.kotlinnotes.ui.base.BaseViewModel

class MainViewModel(private val notesRepository: NotesRepository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer<NoteResult> { noteResult ->
        if (noteResult == null) {
            return@Observer
        }

        when (noteResult) {
            is NoteResult.Success<*> -> {
                viewStateLiveData.value = MainViewState(notes = noteResult.data as? List<Note>)
            }

            is NoteResult.Error -> {
                viewStateLiveData.value = MainViewState(error = noteResult.error)
            }
        }
    }

    private val repositoryNotes = notesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    fun removeNote(note: Note) {
        note.let {
            notesRepository.removeNote(it)
        }
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}