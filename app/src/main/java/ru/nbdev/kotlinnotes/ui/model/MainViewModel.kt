package ru.nbdev.kotlinnotes.ui.model

import android.arch.lifecycle.Observer
import ru.nbdev.kotlinnotes.data.NotesRepository
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.data.model.NoteResult
import ru.nbdev.kotlinnotes.ui.base.BaseViewModel

class MainViewModel : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer<NoteResult> {
        if (it == null) {
            return@Observer
        }

        when (it) {
            is NoteResult.Success<*> -> {
                viewStateLiveData.value = MainViewState(notes = it.data as? List<Note>)
            }

            is NoteResult.Error -> {
                viewStateLiveData.value = MainViewState(error = it.error)
            }
        }
    }

    private val repositoryNotes = NotesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }


    fun remove(note: Note) {
        note.let {
            NotesRepository.removeNote(it)
        }
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}