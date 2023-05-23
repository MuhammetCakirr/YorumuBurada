package com.muhammetcakir.benimurunum.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.databinding.SearchtakipcilerBinding
import com.muhammetcakir.benimurunum.databinding.TakipcilerrcBinding
import com.squareup.picasso.Picasso

class SearchUserFollowAdapter
    (
    private var Takipler: ArrayList<User>,
    private val userdetailclick: UserSearchClick
): RecyclerView.Adapter<SearchUserFollowAdapter.CardViewHolder>() {
    class CardViewHolder(private val cardCellBinding: SearchtakipcilerBinding, private val userdetailclick: UserSearchClick):
        RecyclerView.ViewHolder(cardCellBinding.root)
    {
        fun bindkullanici(user: User)
        {
            Picasso.get().load(user.ImageUrl).into(cardCellBinding.postuserfoto)
            cardCellBinding.username.setText(user.kullaniciadi)
            cardCellBinding.searchlayout.setOnClickListener {
                userdetailclick.Onclick(user)
            }
            cardCellBinding.adsoyad.setText(user.isim)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = SearchtakipcilerBinding.inflate(from, parent, false)
        return SearchUserFollowAdapter.CardViewHolder(binding,userdetailclick)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindkullanici(Takipler[position])
    }

    override fun getItemCount(): Int = Takipler.size
}