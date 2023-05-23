package com.muhammetcakir.benimurunum.Adapter

import android.provider.MediaStore.Video.Media
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.muhammetcakir.benimurunum.Chat
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.Message
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.R
import com.muhammetcakir.benimurunum.databinding.CardmesajlastiklarimBinding
import com.muhammetcakir.benimurunum.databinding.ChatrowBinding
import com.muhammetcakir.benimurunum.databinding.ChatrowfrommeBinding
import com.muhammetcakir.benimurunum.suankikullanicilist
import com.squareup.picasso.Picasso
class MessageAdapter (
    private var mesajlar: ArrayList<Message>,
    private var user: User
        ): RecyclerView.Adapter<MessageAdapter.CardViewHolder>()
{
    private  val MESSAGE_TYPE_LEFT=0
    private  val MESSAGE_TYPE_RIGHT=1

    class CardViewHolder(private val view: View,private val user:User):RecyclerView.ViewHolder(view)
    {
        fun bindkullanici(mesaj:Message)
        {
            if(mesaj.mesajgonderenid.toString()== suankikullanicilist[0].id.toString())
            {
                val txtmesaj: TextView = view.findViewById(R.id.benimmesajben)
                val txtarih: TextView = view.findViewById(R.id.benimmesajtarih)
                val imgUser: ImageView = view.findViewById(R.id.kullanicifotoben)
                txtmesaj.text=mesaj.mesaj.toString()
                txtarih.text=mesaj.mesajatilmatarihi.toString()
                Picasso.get().load(suankikullanicilist[0].ImageUrl).into(imgUser)
            }
            else{
                val txtmesaj: TextView = view.findViewById(R.id.mesajleft)
                val txtarih: TextView = view.findViewById(R.id.mesajtarihleft)
                val imgUser: ImageView = view.findViewById(R.id.mesajkullanicifoto)

                txtmesaj.text=mesaj.mesaj.toString()
                txtarih.text=mesaj.mesajatilmatarihi.toString()
                Picasso.get().load(user.ImageUrl).into(imgUser)

            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        if(viewType==MESSAGE_TYPE_RIGHT)
        {
            val from = LayoutInflater.from(parent.context)
            val binding = ChatrowfrommeBinding.inflate(from, parent, false)
            return MessageAdapter.CardViewHolder(binding.root,user)
        }
        else{
            val from = LayoutInflater.from(parent.context)
            val binding = ChatrowBinding.inflate(from, parent, false)
            return MessageAdapter.CardViewHolder(binding.root,user)
        }

    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindkullanici(mesajlar[position])
    }

    override fun getItemCount(): Int = mesajlar.size
    override fun getItemViewType(position: Int): Int {
        if (mesajlar[position].mesajgonderenid== suankikullanicilist[0].id)
        {
            return MESSAGE_TYPE_RIGHT
        }
        else{
            return MESSAGE_TYPE_LEFT
        }
    }
}