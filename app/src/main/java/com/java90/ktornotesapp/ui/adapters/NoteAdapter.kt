package com.java90.ktornotesapp.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.java90.ktornotesapp.R
import com.java90.ktornotesapp.data.local.entities.Note
import com.java90.ktornotesapp.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var onItemCLickListener: ((Note) -> Unit)? = null

    private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var notes: List<Note>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemNoteBinding.inflate(inflater, parent, false)
        return NoteViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bin(note)
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bin(note: Note) {
            binding.apply {
                tvTitle.text = note.title
                if(!note.isSynced) {
                    ivSynced.setImageResource(R.drawable.ic_cross)
                    tvSynced.text = "No Synced"
                } else {
                    ivSynced.setImageResource(R.drawable.ic_check)
                    tvSynced.text = "Synced"
                }

                val dateFormat = SimpleDateFormat("dd.MM.yy, HH:mm", Locale.getDefault())
                val dateString = dateFormat.format(note.date)
                tvDate.text = dateString

                val drawable = ResourcesCompat.getDrawable(root.resources, R.drawable.circle_shape, null)
                drawable?.let {
                    val drawableWrapper = DrawableCompat.wrap(it)
                    val color = Color.parseColor("#${note.color}")
                    DrawableCompat.setTint(drawableWrapper, color)
                    viewNoteColor.background = drawableWrapper
                }

                itemView.setOnClickListener {
                    onItemCLickListener?.let { click ->
                        click(note)
                    }
                }
            }
        }
    }

    fun setOnItemClickListener(onItemClick: (Note) -> Unit) {
        this.onItemCLickListener = onItemClick
    }
}