package com.muhammetcakir.benimurunum.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.KullanicilarArrayList
import com.muhammetcakir.benimurunum.Models.Comment
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.databinding.CardcommentBinding
import com.muhammetcakir.benimurunum.databinding.TakipcilerrcBinding
import com.squareup.picasso.Picasso

class CommentAdapter
    (
    private var Yorumlar: ArrayList<Comment>,
    private val userdetailclick: UserSearchClick
) : RecyclerView.Adapter<CommentAdapter.CardViewHolder>()
{
    class CardViewHolder(
        private val cardCellBinding: CardcommentBinding,
        private val userdetailclick: UserSearchClick
    ) : RecyclerView.ViewHolder(cardCellBinding.root)
    {
        var kullaniciadi: String = ""
        var imageurl: String = ""
        fun bindyorum(comment: Comment) {
            for (user in KullanicilarArrayList)
            {
                if (user.id.toString()==comment.yorumyapankisiid.toString())
                {
                    kullaniciadi=user.kullaniciadi.toString()
                    imageurl=user.ImageUrl.toString()
                    Picasso.get().load(imageurl).into(cardCellBinding.postuserfoto)
                    cardCellBinding.yorumcontent.text=comment.yorum.toString()
                    cardCellBinding.username.text=kullaniciadi.toString()
                    cardCellBinding.tarih.text=comment.yorumatilmatarihi.toString()

                    cardCellBinding.searchlayout.setOnClickListener {
                        userdetailclick.Onclick(user)
                    }
                }
            }


        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentAdapter.CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardcommentBinding.inflate(from, parent, false)
        return CommentAdapter.CardViewHolder(binding, userdetailclick)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CardViewHolder, position: Int) {
        holder.bindyorum(Yorumlar[position])
    }

    override fun getItemCount(): Int = Yorumlar.size
}
