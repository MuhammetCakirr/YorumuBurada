package com.muhammetcakir.benimurunum.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.muhammetcakir.benimurunum.BegenilerList
import com.muhammetcakir.benimurunum.ClickListeners.PostClick
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.KullanicilarArrayList
import com.muhammetcakir.benimurunum.Models.Notification
import com.muhammetcakir.benimurunum.Models.Post
import com.muhammetcakir.benimurunum.PostlarArrayList
import com.muhammetcakir.benimurunum.YorumlarList
import com.muhammetcakir.benimurunum.databinding.CardnotificationBinding
import com.muhammetcakir.benimurunum.databinding.ProfilercyclerwiewBinding
import com.squareup.picasso.Picasso

private var auth: FirebaseAuth = FirebaseAuth.getInstance()
val gidilecekPostList: ArrayList<Post> = ArrayList()

class NotificationAdapter
    (
    private var Bildirimler: ArrayList<Notification>,
    private val userdetailclick: UserSearchClick,
    private val postdetailclick: PostClick
) : RecyclerView.Adapter<NotificationAdapter.CardViewHolder>() {
    class CardViewHolder(
        private val binding: CardnotificationBinding,
        private val postdetailclick: PostClick,
        private val userdetailclick: UserSearchClick
    ) : RecyclerView.ViewHolder(binding.root) {
        var kullaniciadi: String = ""
        var imageurl: String = ""
        fun bindnotification(not: Notification) {
            for (user in KullanicilarArrayList) {
                if (not.olayiyapankisi.toString() == user.id.toString()) {
                    kullaniciadi = user.kullaniciadi.toString()
                    imageurl = user.ImageUrl.toString()
                    if (not.olaynum == "1") //Takip etme olayı ise
                    {
                        Picasso.get().load(imageurl).into(binding.postfoto)
                        binding.bildirimyazisi.text = kullaniciadi + " seni takip etti."
                        binding.notlayout.setOnClickListener {
                            userdetailclick.Onclick(user)
                        }
                        binding.tarih.text=not.olayolmatarihi.toString()
                    }
                    if (not.olaynum == "2")  //Beğenme olayı ise
                    {
                        binding.notlayout.setOnClickListener {
                            userdetailclick.Onclick(user)
                        }

                        Picasso.get().load(imageurl).into(binding.postfoto)
                        binding.bildirimyazisi.text = kullaniciadi + " gönderini beğendi."
                        binding.tarih.text=not.olayolmatarihi.toString()

                    }
                    if (not.olaynum == "3") //Yorum yapma  olayı ise
                    {
                        binding.notlayout.setOnClickListener {
                            userdetailclick.Onclick(user)
                        }
                        Picasso.get().load(imageurl).into(binding.postfoto)
                        binding.bildirimyazisi.text = kullaniciadi + " gönderine yorum yaptı."
                        binding.tarih.text=not.olayolmatarihi.toString()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardnotificationBinding.inflate(from, parent, false)
        return NotificationAdapter.CardViewHolder(binding, postdetailclick, userdetailclick)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindnotification(Bildirimler[position])
    }

    override fun getItemCount(): Int = Bildirimler.size
}