package ru.nbdev.kotlingb.ui.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.nbdev.kotlingb.data.NotesRepository

class MainViewModel : ViewModel() {

    private val viewStateLiveData = MutableLiveData<MainViewState>()

    init {
        viewStateLiveData.value = MainViewState(NotesRepository.notes)
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}