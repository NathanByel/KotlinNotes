package ru.nbdev.kotlinnotes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.nbdev.kotlinnotes.data.NotesRepository
import ru.nbdev.kotlinnotes.data.provider.FireStoreProvider
import ru.nbdev.kotlinnotes.ui.model.MainViewModel
import ru.nbdev.kotlinnotes.ui.model.NoteViewModel
import ru.nbdev.kotlinnotes.ui.model.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get<FirebaseAuth>(), get<FirebaseFirestore>()) }
    single { NotesRepository(get<FireStoreProvider>()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get<NotesRepository>()) }
}

val mainModule = module {
    viewModel { MainViewModel(get<NotesRepository>()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get<NotesRepository>()) }
}