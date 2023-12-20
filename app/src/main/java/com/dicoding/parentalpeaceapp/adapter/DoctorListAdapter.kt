package com.dicoding.parentalpeaceapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dicoding.parentalpeaceapp.databinding.DoctorItemBinding
import com.dicoding.parentalpeaceapp.response.DataDoctorItem

class DoctorListAdapter :
    ListAdapter<DataDoctorItem, DoctorListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DoctorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class MyViewHolder(private val binding: DoctorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataDoctorItem) {
            with(binding) {
                tvName.text = data.name
                tvOnline.text = data.status
                tvSpesialis.text = data.specialis
                tvRating.text = data.rate
                tvPrice.text = data.harga

                val requestOptions = RequestOptions().transform(RoundedCorners(10))
                Glide.with(itemView.context)
                    .load(data.imageUrl)
                    .apply(requestOptions)
                    .into(ivArticles)

/*                itemView.setOnClickListener {
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
                }*/
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataDoctorItem>() {
            override fun areItemsTheSame(oldItem: DataDoctorItem, newItem:DataDoctorItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataDoctorItem, newItem: DataDoctorItem): Boolean {
                return oldItem.doctorId == newItem.doctorId
            }
        }
    }
}