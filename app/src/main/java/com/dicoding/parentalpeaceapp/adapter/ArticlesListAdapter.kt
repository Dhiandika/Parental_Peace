package com.dicoding.parentalpeaceapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dicoding.parentalpeaceapp.databinding.ArticlesItemBinding
import com.dicoding.parentalpeaceapp.response.DataItem
import com.dicoding.parentalpeaceapp.ui.detailarticle.DetailArticelActivity

class ArticlesListAdapter :
    ListAdapter<DataItem, ArticlesListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ArticlesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class MyViewHolder(private val binding: ArticlesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItem) {
            with(binding) {
                tvTittle.text = data.title
                tvDescription.text = data.content

                val requestOptions = RequestOptions().transform(RoundedCorners(10))
                Glide.with(itemView.context)
                    .load(data.articlePicture)
                    .apply(requestOptions)
                    .into(ivArticles)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailArticelActivity::class.java)
                    intent.putExtra(DetailArticelActivity.DETAIL_ARTICLE, data)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            androidx.core.util.Pair(ivArticles, "picture"),
                            androidx.core.util.Pair(tvTittle, "tittle"),
                            androidx.core.util.Pair(tvDescription, "description")
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem:DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.articleId == newItem.articleId
            }
        }
    }
}