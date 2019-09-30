package ru.nbdev.kotlinnotes.data

import android.arch.lifecycle.LiveData
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.data.entity.User
import ru.nbdev.kotlinnotes.data.model.NoteResult
import ru.nbdev.kotlinnotes.data.provider.FireStoreProvider
import ru.nbdev.kotlinnotes.data.provider.RemoteDataProvider

object NotesRepository {
    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes(): LiveData<NoteResult> {
        return remoteProvider.subscribeToAllNotes()
    }

    fun saveNote(note: Note): LiveData<NoteResult> {
        return remoteProvider.saveNote(note)
    }

    fun removeNote(note: Note): LiveData<NoteResult> {
        return remoteProvider.removeNote(note)
    }

    fun getNoteById(id: String): LiveData<NoteResult> {
        return remoteProvider.getNoteById(id)
    }

    fun getCurrentUser(): LiveData<User?> {
        return remoteProvider.getCurrentUser()
    }
}