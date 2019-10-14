package ru.nbdev.kotlinnotes.ui

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.nbdev.kotlinnotes.data.NotesRepository
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.data.model.NoteResult
import ru.nbdev.kotlinnotes.ui.model.MainViewModel
import ru.nbdev.kotlinnotes.ui.model.MainViewState

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mock<NotesRepository>()
    private val notesLiveData = MutableLiveData<NoteResult>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        reset(mockRepository)
        whenever(mockRepository.getNotes()).thenReturn(notesLiveData)
        whenever(mockRepository.removeNote(any())).thenReturn(notesLiveData)
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes once`() {
        verify(mockRepository, times(1)).getNotes()
    }

    @Test
    fun `should return Notes`() {
        var result: List<Note>? = null
        val testData = listOf(Note("1"), Note("2"))
        viewModel.getViewState().observeForever {
            result = it?.data
        }
        notesLiveData.value = NoteResult.Success(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        notesLiveData.value = NoteResult.Error(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onCleared()
        assertFalse(notesLiveData.hasObservers())
    }

    @Test
    fun `deleteNote should return list without removed note`() {
        val testNote = Note("1")
        val testNotes = listOf(testNote, Note("2"))
        val testNotesRemoved = listOf(Note("2"))

        var result: MainViewState? = null
        val testData = MainViewState(testNotesRemoved, null)
        viewModel.getViewState().observeForever {
            result = it
        }
        notesLiveData.value = NoteResult.Success(testNotes)

        viewModel.removeNote(testNote)
        notesLiveData.value = NoteResult.Success(testNotesRemoved)
        assertEquals(testData.notes, result?.notes)
    }

    @Test
    fun `deleteNote should return error`() {
        val testNote = Note("1")
        val testNotes = listOf(testNote, Note("2"))
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        notesLiveData.value = NoteResult.Success(testNotes)

        viewModel.removeNote(testNote)
        notesLiveData.value = NoteResult.Error(error = testData)
        assertEquals(testData, result)
    }
}