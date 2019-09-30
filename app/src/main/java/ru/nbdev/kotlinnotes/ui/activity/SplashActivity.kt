package ru.nbdev.kotlinnotes.ui.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Handler
import ru.nbdev.kotlinnotes.ui.base.BaseActivity
import ru.nbdev.kotlinnotes.ui.model.SplashViewModel
import ru.nbdev.kotlinnotes.ui.model.SplashViewState

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    companion object {
        private const val START_DELAY: Long = 1000
    }

    override val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    override val layoutRes: Int? = null

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean?) {
        if (data != null && data) {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }
}