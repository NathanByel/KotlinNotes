package ru.nbdev.kotlinnotes.ui.base

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import ru.nbdev.kotlinnotes.R
import ru.nbdev.kotlinnotes.data.errors.NoAuthException

abstract class BaseActivity<D, S : BaseViewState<D>> : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN: Int = 12121
    }

    abstract val viewModel: BaseViewModel<D, S>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutRes?.let {
            setContentView(it)
        }

        viewModel.getViewState().observe(this, Observer<S> {
            if (it == null) return@Observer
            if (it.error != null) {
                renderError(it.error)
                return@Observer
            }
            renderData(it.data)
        })
    }

    abstract fun renderData(data: D)

    protected fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startAuth()
            else -> error.message?.let { showError(it) }
        }
    }

    private fun startAuth() {
        val providers = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.AnonymousBuilder().build()
        )

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.android_robot)
                        .setTheme(R.style.LoginStyle)
                        .setAvailableProviders(providers)
                        .build()
                , RC_SIGN_IN
        )
    }

    protected fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
    }
}