package ru.nbdev.kotlinnotes.ui.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

abstract class BaseActivity<D, S : BaseViewState<D>> : AppCompatActivity() {

    abstract val viewModel: BaseViewModel<D, S>
    abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutRes)
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
        error.message?.let { showError(it) }
    }

    protected fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}