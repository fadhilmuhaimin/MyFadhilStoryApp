package com.fadhil.myfadhilstoryapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fadhil.myfadhilstoryapp.data.remote.response.ListStoryItem
import com.fadhil.myfadhilstoryapp.databinding.ItemListBinding
import com.fadhil.myfadhilstoryapp.detail.DetailActivity

class StoryAdapter() : ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryItem) {
            binding.tvItemName.text = item.name
            binding.tvItemDescription.text = item.description
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error)
                )
                .into(binding.imgProfile)

            binding.root.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imgProfile, "image"),
                        Pair(binding.tvItemName, "title"),
                        Pair(binding.tvItemDescription, "description"),
                    )
                binding.root.context.startActivity(
                    Intent(binding.root.context, DetailActivity::class.java)
                        .putExtra(DetailActivity.KEY_DETAIL, item),
                    optionsCompat.toBundle()
                )
            }
        }
    }


    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListStoryItem> =
            object : DiffUtil.ItemCallback<ListStoryItem>() {
                override fun areItemsTheSame(
                    oldUser: ListStoryItem,
                    newUser: ListStoryItem
                ): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldUser: ListStoryItem,
                    newUser: ListStoryItem
                ): Boolean {
                    return oldUser == newUser
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }
}