package ru.nbdev.kotlinnotes.common

import android.content.Context
import ru.nbdev.kotlinnotes.R
import ru.nbdev.kotlinnotes.data.entity.Note

fun Note.Color.getColorInt(context: Context) = android.support.v4.content.ContextCompat.getColor(
        context, getColorRes()
)

fun Note.Color.getColorRes(): Int = when (this) {
    Note.Color.WHITE -> R.color.white
    Note.Color.YELLOW -> R.color.yellow
    Note.Color.GREEN -> R.color.green
    Note.Color.BLUE -> R.color.blue
    Note.Color.RED -> R.color.red
    Note.Color.VIOLET -> R.color.violet
    Note.Color.PINK -> R.color.pink
}