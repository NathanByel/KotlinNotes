package ru.nbdev.kotlinnotes.ui.model

import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.ui.base.BaseViewState

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null) : BaseViewState<List<Note>?>(notes, error)