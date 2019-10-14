package ru.nbdev.kotlinnotes.note

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.mockito.Mockito.*
import ru.nbdev.kotlinnotes.R
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.notNullAny
import ru.nbdev.kotlinnotes.ui.activity.MainActivity
import ru.nbdev.kotlinnotes.ui.activity.NoteActivity
import ru.nbdev.kotlinnotes.ui.model.NoteViewModel
import ru.nbdev.kotlinnotes.ui.model.NoteViewState

class NoteActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    private val model: NoteViewModel = mock(NoteViewModel::class.java)
    private val viewStateLiveData = MutableLiveData<NoteViewState>()

    private val testNote = Note("aaa", "title1", "body1")

    @Before
    fun setup() {
        loadKoinModules(
                listOf(
                        module {
                            viewModel(override = true) { model }
                        }
                )
        )

        doReturn(viewStateLiveData).`when`(model).getViewState()
        doNothing().`when`(model).loadNote(testNote.id)
        doNothing().`when`(model).save(notNullAny(Note::class.java))

        Intent().apply {
            putExtra(NoteActivity::class.java.name + "extra.NOTE", testNote.id)
        }.let {
            activityTestRule.launchActivity(it)
        }
    }

    @Test
    fun should_show_color_picker() {
        // FIXME
        //onView(withId(R.id.palette)).perform(click())
        onView(withText(R.string.note_change_color)).perform(click())
        onView(withId(R.id.color_picker)).check(matches(isCompletelyDisplayed()))
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}