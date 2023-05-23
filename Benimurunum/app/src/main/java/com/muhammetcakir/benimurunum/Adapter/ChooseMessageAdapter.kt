package com.muhammetcakir.benimurunum.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammetcakir.benimurunum.BenimMesajlarList
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.benveoMesajlarList
import com.muhammetcakir.benimurunum.databinding.CardmesajlastiklarimBinding
import com.muhammetcakir.benimurunum.databinding.TakipcilerrcBinding
import com.muhammetcakir.benimurunum.suankikullanicilist
import com.squareup.picasso.Picasso

class ChooseMessageAdapter (
    private var Takipler: ArrayList<User>,
    private val userdetailclick: UserSearchClick
): RecyclerView.Adapter<ChooseMessageAdapter.CardViewHolder>()
{
    class CardViewHolder(private val cardCellBinding: CardmesajlastiklarimBinding, private val userdetailclick: UserSearchClick):RecyclerView.ViewHolder(cardCellBinding.root)
    {


        fun bindkullanici(user: User)
        {
            println("LİSTE " + BenimMesajlarList.count().toString())
            for(mesajlar in BenimMesajlarList)
            {

                if (mesajlar.mesajalanid== suankikullanicilist[0].id.toString() && mesajlar.mesajgonderenid==user.id.toString())
                {
                    benveoMesajlarList.add(mesajlar)
                }
                else if (mesajlar.mesajgonderenid==suankikullanicilist[0].id.toString() && mesajlar.mesajalanid==user.id.toString()){
                    benveoMesajlarList.add(mesajlar)
                }
            }
           val sayi=benveoMesajlarList.count()
            val sonmesaj= benveoMesajlarList[sayi-1].mesaj.toString()

            Picasso.get().load(user.ImageUrl).into(cardCellBinding.kullanicifoto)
            cardCellBinding.kullaniciadi.setText(user.kullaniciadi)
            cardCellBinding.sonmesaj.text=sonmesaj.toString()
            cardCellBinding.layout.setOnClickListener {
                userdetailclick.Onclick(user)
            }
            cardCellBinding.sonmesajtarih.text=benveoMesajlarList[sayi-1].sonmesajatilmatarihi.toString()
            benveoMesajlarList.clear()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardmesajlastiklarimBinding.inflate(from, parent, false)
      //  val binding2 = CardmesajlastiklarimBinding.inflate(from, parent, true)

        return ChooseMessageAdapter.CardViewHolder(binding,userdetailclick)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindkullanici(Takipler[position])
    }

    override fun getItemCount(): Int = Takipler.size
}