package ru.nbdev.kotlinnotes.data.provider

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.data.entity.User
import ru.nbdev.kotlinnotes.data.errors.NoAuthException
import ru.nbdev.kotlinnotes.data.model.NoteResult

class FireStoreProvider : RemoteDataProvider {


    companion object {
        private const val USERS_COLLECTION = "users"
        private const val NOTES_COLLECTION = "notes"
    }

    private val store = FirebaseFirestore.getInstance()

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    override fun getCurrentUser(): LiveData<User?> {
        val data = MutableLiveData<User>()

        data.value = currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        }

        return data
    }

    /*override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User>().apply {
        value = currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        }
    }*/

    private fun getUserNotesCollection(): CollectionReference {
        currentUser?.let {
            return store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()
    }

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        try {
            getUserNotesCollection()
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            result.value = NoteResult.Error(e)
                        } else if (snapshot != null) {
                            val notes = snapshot.documents.map { it.toObject(Note::class.java) }
                            NoteResult.Success(notes)

                            result.value = NoteResult.Success(notes)
                        }

                    }
        } catch (e: Throwable) {
            result.value = NoteResult.Error(e)
        }

        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        try {
            getUserNotesCollection()
                    .document(id).get()
                    .addOnSuccessListener { snapshot ->
                        result.value = NoteResult.Success(snapshot.toObject(Note::class.java))
                    }.addOnFailureListener {
                        result.value = NoteResult.Error(it)
                    }
        } catch (e: Throwable) {
            result.value = NoteResult.Error(e)
        }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        try {
            getUserNotesCollection()
                    .document(note.id)
                    .set(note).addOnSuccessListener {
                        //Timber.d { "Note $note is saved" }
                        result.value = NoteResult.Success(note)
                    }.addOnFailureListener {
                        //Timber.e(it) { "Error saving note $note with message: ${it.message}" }
                        result.value = NoteResult.Error(it)
                    }
        } catch (e: Throwable) {
            result.value = NoteResult.Error(e)
        }
        return result
    }

    override fun removeNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        try {
            getUserNotesCollection()
                    .document(note.id).delete()
                    .addOnSuccessListener {
                        result.value = NoteResult.Success(note)
                    }.addOnFailureListener {
                        result.value = NoteResult.Error(it)
                    }
        } catch (e: Throwable) {
            result.value = NoteResult.Error(e)
        }

        return result
    }
}