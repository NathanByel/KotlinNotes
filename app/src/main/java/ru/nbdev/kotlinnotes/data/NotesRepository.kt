package ru.nbdev.kotlinnotes.data

import android.arch.lifecycle.LiveData
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.data.entity.User
import ru.nbdev.kotlinnotes.data.model.NoteResult
import ru.nbdev.kotlinnotes.data.provider.RemoteDataProvider

class NotesRepository(private val remoteProvider: RemoteDataProvider) {
    fun getNotes(): LiveData<NoteResult> = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note): LiveData<NoteResult> = remoteProvider.saveNote(note)
    fun removeNote(note: Note): LiveData<NoteResult> = remoteProvider.removeNote(note)
    fun getNoteById(id: String): LiveData<NoteResult> = remoteProvider.getNoteById(id)
    fun getCurrentUser(): LiveData<User?> = remoteProvider.getCurrentUser()
}