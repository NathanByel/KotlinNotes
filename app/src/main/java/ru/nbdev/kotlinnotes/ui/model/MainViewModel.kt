package ru.nbdev.kotlinnotes.ui.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.nbdev.kotlinnotes.data.NotesRepository
import ru.nbdev.kotlinnotes.data.entity.Note

class MainViewModel : ViewModel() {

    private val viewStateLiveData = MutableLiveData<MainViewState>()

    /*init {
        NotesRepository.getNotes().observeForever {
            it?.let {
                viewStateLiveData.value = MainViewState(it)
            }
        }
    }*/

    init {
        NotesRepository.getNotes().observeForever {
            viewStateLiveData.value = viewStateLiveData.value?.copy(notes = it!!)
                    ?: MainViewState(it!!)
        }
    }

    fun remove(note: Note) {
        note.let {
            NotesRepository.removeNote(it)
        }
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}