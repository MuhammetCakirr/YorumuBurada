package com.muhammetcakir.benimurunum.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammetcakir.benimurunum.ClickListeners.PostClick
import com.muhammetcakir.benimurunum.Models.Post
import com.muhammetcakir.benimurunum.Profile
import com.muhammetcakir.benimurunum.databinding.ProfilercyclerwiewBinding
import com.squareup.picasso.Picasso

class ProfilePostsAdapter(
    private val postlar: ArrayList<Post>,
    private val postdetailclick: PostClick
): RecyclerView.Adapter<ProfilePostsAdapter.CardViewHolder>()
{
    class CardViewHolder( private val cardCellBinding: ProfilercyclerwiewBinding,private val postdetailclick: PostClick):RecyclerView.ViewHolder(cardCellBinding.root)
    {
        fun bindpost(post: Post)
        {
            Picasso.get().load(post.ImageUrl).into(cardCellBinding.profildekiurunler)
            cardCellBinding.profildekiurunler.setOnClickListener{
                postdetailclick.onClick(post)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ProfilercyclerwiewBinding.inflate(from, parent, false)
        return CardViewHolder(binding,postdetailclick)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindpost(postlar[position])
    }

    override fun getItemCount(): Int = postlar.size

}