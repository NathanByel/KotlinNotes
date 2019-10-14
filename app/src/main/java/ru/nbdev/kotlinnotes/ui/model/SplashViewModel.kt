package ru.nbdev.kotlinnotes.ui.model

import ru.nbdev.kotlinnotes.data.NotesRepository
import ru.nbdev.kotlinnotes.data.errors.NoAuthException
import ru.nbdev.kotlinnotes.ui.base.BaseViewModel

class SplashViewModel(private val notesRepository: NotesRepository) : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        notesRepository.getCurrentUser().observeForever { user ->
            viewStateLiveData.value = if (user != null) {
                SplashViewState(authenticated = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}