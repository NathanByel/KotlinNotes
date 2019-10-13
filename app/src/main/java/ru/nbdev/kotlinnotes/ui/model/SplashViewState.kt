package ru.nbdev.kotlinnotes.ui.model

import ru.nbdev.kotlinnotes.ui.base.BaseViewState

class SplashViewState(authenticated: Boolean? = null, error: Throwable? = null) : BaseViewState<Boolean?>(authenticated, error)