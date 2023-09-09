package com.example.githubusernew.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.githubusernew.data.remote.model.FollowUserResponseItem
import com.example.githubusernew.databinding.ItemRowUserBinding
import com.example.githubusernew.ui.notediffcallback.NoteDiffCallbackFollow

class FollowAdapter(private val list: ArrayList<FollowUserResponseItem>): RecyclerView.Adapter<FollowAdapter.UserViewHolder>(){

    fun setListUser(listNotes: List<FollowUserResponseItem>) {
        val diffCallback = NoteDiffCallbackFollow(this.list, listNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.list.clear()
        this.list.addAll(listNotes)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class UserViewHolder(val binding: ItemRowUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(user: FollowUserResponseItem) {

            Glide.with(itemView)
                .load(user.avatarUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivItemAvatarUrl)

            binding.tvItemUsername.text = user.login
            binding.tvItemHtmlUrl.text = user.htmlUrl
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowAdapter.UserViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowAdapter.UserViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}