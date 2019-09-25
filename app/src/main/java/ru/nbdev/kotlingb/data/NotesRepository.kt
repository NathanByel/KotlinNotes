package ru.nbdev.kotlingb.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ru.nbdev.kotlingb.data.entity.Note
import java.util.*

object NotesRepository {
    private val notesLiveData = MutableLiveData<List<Note>>()
    private var notes: MutableList<Note> = mutableListOf()

    init {
        for (i in 1..50) {
            notes.add(
                    Note(UUID.randomUUID().toString(),
                            "Заметка $i",
                            "Текст заметки $i.\n" +
                                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut a eros quam. " +
                                    "Integer vitae libero orci. Mauris at dolor aliquam diam rhoncus commodo ac " +
                                    "sit amet erat. Proin luctus gravida ultricies. Ut tempus, tortor at mollis " +
                                    "commodo, orci sapien iaculis nisi, at pretium mauris lacus id magna. " +
                                    "Pellentesque mollis feugiat lobortis. Aenean mollis eu nibh et fermentum.",
                            Note.Color.values().random()
                    )
            )
        }

        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    fun removeNote(note: Note) {
        notes.remove(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note) {
        val i = notes.indexOf(note)
        if (i >= 0) {
            notes[i] = note
            return
        }

        notes.add(note)
    }
}