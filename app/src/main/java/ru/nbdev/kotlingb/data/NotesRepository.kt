package ru.nbdev.kotlingb.data

import ru.nbdev.kotlingb.data.entity.Note
import java.util.*

object NotesRepository {
    var notes: MutableList<Note> = mutableListOf()

    init {
        for (i in 1..50) {
            notes.add(
                    Note("Заметка $i",
                            "Текст заметки $i./nLorem ipsum dolor sit amet, " +
                                    "consectetur adipiscing elit. Ut a eros quam. Integer vitae libero orci. " +
                                    "Mauris at dolor aliquam diam rhoncus commodo ac sit amet erat. Proin luctus " +
                                    "gravida ultricies. Ut tempus, tortor at mollis commodo, orci sapien iaculis nisi, " +
                                    "at pretium mauris lacus id magna. Pellentesque mollis feugiat lobortis. Aenean mollis eu nibh et fermentum.",
                            Random().nextInt()
                    )
            )
        }
    }
}