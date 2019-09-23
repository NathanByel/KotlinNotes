package ru.nbdev.kotlingb.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.nbdev.kotlingb.R
import java.util.*

@Parcelize
data class Note(
        val id: String,
        val title: String,
        val text: String,
        val color: Color = Color.WHITE,
        val lastChanged: Date = Date()
) : Parcelable {

    enum class Color(val resColor: Int) {
        WHITE(R.color.white),
        YELLOW(R.color.yellow),
        GREEN(R.color.green),
        BLUE(R.color.blue),
        RED(R.color.red),
        VIOLET(R.color.violet),
        PINK(R.color.pink)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false
        return true
    }
}