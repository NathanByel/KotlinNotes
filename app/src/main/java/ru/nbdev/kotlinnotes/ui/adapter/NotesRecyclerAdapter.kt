package ru.nbdev.kotlinnotes.ui.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recycler_item_note.view.*
import ru.nbdev.kotlinnotes.R
import ru.nbdev.kotlinnotes.data.entity.Note

class NotesRecyclerAdapter(val onItemClick: ((Note) -> Unit)? = null,
                           val onLongItemClick: ((Note) -> Boolean)? = null) : RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                    R.layout.recycler_item_note,
                    parent,
                    false
            )
    )

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) = viewHolder.bind(notes[pos])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textviewNoteTitle = itemView.textview_note_title
        private val textviewNoteText = itemView.textview_note_text

        fun bind(note: Note) {
            textviewNoteTitle.text = note.title
            textviewNoteText.text = note.text

            val cardView = itemView as CardView
            cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, note.color.resColor))

            itemView.setOnClickListener {
                onItemClick?.invoke(note)
            }

            onLongItemClick?.let {
                itemView.setOnLongClickListener {
                    onLongItemClick.invoke(note)
                    return@setOnLongClickListener true
                }
            }
        }
    }

}