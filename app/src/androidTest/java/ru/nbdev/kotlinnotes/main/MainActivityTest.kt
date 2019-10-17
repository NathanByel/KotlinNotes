package ru.nbdev.kotlinnotes.main

import android.arch.lifecycle.MutableLiveData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doReturn

import ru.nbdev.kotlinnotes.R
import ru.nbdev.kotlinnotes.data.entity.Note
import ru.nbdev.kotlinnotes.notNullAny
import ru.nbdev.kotlinnotes.ui.activity.MainActivity
import ru.nbdev.kotlinnotes.ui.adapter.NotesRecyclerAdapter
import ru.nbdev.kotlinnotes.ui.model.MainViewModel
import ru.nbdev.kotlinnotes.ui.model.MainViewState

class MainActivityTest {
    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    private val model: MainViewModel = Mockito.mock(MainViewModel::class.java)
    private val viewStateLiveData = MutableLiveData<MainViewState>()
    private val testNotes = listOf(
            Note("aaa", "title1", "body1"),
            Note("bbb", "title2", "body2"),
            Note("ccc", "title3", "body3")
    )

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
        doNothing().`when`(model).removeNote(notNullAny(Note::class.java))
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(MainViewState(notes = testNotes))
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_data_is_displayed() {
        onView(withId(R.id.recycler_main_notes)).perform(scrollToPosition<NotesRecyclerAdapter.ViewHolder>(1))
        onView(withText(testNotes[1].text)).check(matches(isDisplayed()))
    }
}