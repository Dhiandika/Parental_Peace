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
import com.dicoding.parentalpeaceapp.databinding.DoctorItemBinding
import com.dicoding.parentalpeaceapp.response.DataDoctorItem
import com.dicoding.parentalpeaceapp.ui.detailarticle.DetailArticelActivity
import com.dicoding.parentalpeaceapp.ui.payment.PaymentActivity

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
                chatBtn

                val requestOptions = RequestOptions().transform(RoundedCorners(10))
                Glide.with(itemView.context)
                    .load(data.imageUrl)
                    .apply(requestOptions)
                    .into(ivArticles)

                binding.chatBtn.setOnClickListener {
                    val intent = Intent(itemView.context, PaymentActivity::class.java)
                    intent.putExtra(PaymentActivity.PAYMENT, data)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            androidx.core.util.Pair(chatBtn, "button")
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }

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