package com.example.githubusernew.ui.notediffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.githubusernew.data.local.FavoriteEntity
import com.example.githubusernew.data.remote.model.ItemsItem

/**
 * NoteDiffCallback untuk memeriksa perubahan yang ada pada listNotes
 * Jadi jika ada perubahan pada listNotes, maka akan memperbarui secara otomatis.
 * NoteDiffCallback digunakan sebagai pengganti notifyDataSetChanged
 * yang fungsinya sama-sama untuk melakukan pembaharuan item pada RecyclerView.
 */
class NoteDiffCallbackList(private val oldNoteList: List<FavoriteEntity>, private val newNoteList: List<FavoriteEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldNoteList.size
    override fun getNewListSize(): Int = newNoteList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNoteList[oldItemPosition].id == newNoteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldNoteList[oldItemPosition]
        val newNote = newNoteList[newItemPosition]
        return oldNote.avatar_url == newNote.avatar_url && oldNote.login == newNote.login && oldNote.html_url == newNote.html_url
    }
}