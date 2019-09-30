package ru.nbdev.kotlinnotes.ui.model

import ru.nbdev.kotlinnotes.data.NotesRepository
import ru.nbdev.kotlinnotes.data.errors.NoAuthException
import ru.nbdev.kotlinnotes.ui.base.BaseViewModel

class SplashViewModel : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        NotesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(authenticated = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}