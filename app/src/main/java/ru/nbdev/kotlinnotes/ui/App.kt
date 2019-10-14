package ru.nbdev.kotlinnotes.ui

import android.app.Application
import org.koin.android.ext.android.startKoin
import ru.nbdev.kotlinnotes.di.appModule
import ru.nbdev.kotlinnotes.di.mainModule
import ru.nbdev.kotlinnotes.di.noteModule
import ru.nbdev.kotlinnotes.di.splashModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}