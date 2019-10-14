package ru.nbdev.kotlinnotes

import timber.log.Timber

class ProductionTree : Timber.DebugTree() {

    override fun log(priority: Int, message: String?, vararg args: Any?) {
        super.log(priority, message, *args)
    }
}