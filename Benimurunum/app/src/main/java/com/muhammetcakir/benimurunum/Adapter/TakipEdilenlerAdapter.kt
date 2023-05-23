package com.muhammetcakir.benimurunum.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.databinding.TakipcilerrcBinding
import com.muhammetcakir.benimurunum.followArrayList
import com.squareup.picasso.Picasso
private var auth: FirebaseAuth = FirebaseAuth.getInstance()
private var db: DatabaseReference = Firebase.database.reference
class TakipEdilenlerAdapter
    (
    private var Takipler: ArrayList<User>,
    private val userdetailclick: UserSearchClick
): RecyclerView.Adapter<TakipEdilenlerAdapter.CardViewHolder>()
{
    class CardViewHolder(private val cardCellBinding: TakipcilerrcBinding, private val userdetailclick: UserSearchClick):RecyclerView.ViewHolder(cardCellBinding.root)
    {
        fun bindkullanici(user: User)
        {
            Picasso.get().load(user.ImageUrl).into(cardCellBinding.postuserfoto)
            cardCellBinding.username.setText(user.kullaniciadi)
            cardCellBinding.searchlayout.setOnClickListener {
                userdetailclick.Onclick(user)
            }
            var id:String=""

            cardCellBinding.kaldir.setOnClickListener {
                for (follow in followArrayList)
                {
                    if(follow.takipedenkisiid== auth.currentUser!!.uid.toString() && follow.takipedilenkisiid==user.id.toString())
                    {
                        id=follow.id.toString()
                    }

                }
                TakiptenCik(id.toString())
            }
            cardCellBinding.adsoyad.setText(user.isim)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = TakipcilerrcBinding.inflate(from, parent, false)
        return TakipEdilenlerAdapter.CardViewHolder(binding,userdetailclick)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindkullanici(Takipler[position])
    }

    override fun getItemCount(): Int = Takipler.size
}
private fun TakiptenCik(id: String) {
    db.child("Follows").child(id).removeValue()
        .addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
                //back
            }
        }.addOnFailureListener { exception ->

        }

}