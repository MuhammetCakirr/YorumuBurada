package com.muhammetcakir.benimurunum.Adapter
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.muhammetcakir.benimurunum.*
import com.muhammetcakir.benimurunum.ClickListeners.CommentClick
import com.muhammetcakir.benimurunum.ClickListeners.NumberOfLike
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.NotificationData
import com.muhammetcakir.benimurunum.Models.Post
import com.muhammetcakir.benimurunum.Models.PushNotification
import com.muhammetcakir.benimurunum.Models.RetrofitInstance
import com.muhammetcakir.benimurunum.databinding.PostrcyclerviewBinding

import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

private var auth: FirebaseAuth = FirebaseAuth.getInstance()
val database = FirebaseDatabase.getInstance().getReference("SavePosts")
val database2 = FirebaseDatabase.getInstance().getReference("Likes")
val database3 = FirebaseDatabase.getInstance().getReference("Notification")
val database4 = FirebaseDatabase.getInstance().getReference("Comments")
var topic = ""
class AnasayfaAdapter(
    private var Postlar: ArrayList<Post>,
    private val userdetailclick: UserSearchClick,
    private val yorumdetailclick: CommentClick,
    private val begenisayisiclick: NumberOfLike
) : RecyclerView.Adapter<AnasayfaAdapter.CardViewHolder>() {
    class CardViewHolder(
        private val binding: PostrcyclerviewBinding,
        private val userdetailclick: UserSearchClick,
        private val yorumdetailclick: CommentClick,
        private val begenisayisiclick: NumberOfLike
    ) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bindpost(post: Post) {
            var begenisayaci: Int = 0
            var kullaniciadi: String = ""
            var imageurl: String = ""
            var spostid: String = ""
            var likeid: String = ""
            var kullaniciadi2:String=""
            for (user in KullanicilarArrayList)
            {
                if(user.id.toString()==auth.currentUser!!.uid.toString())
                {
                    kullaniciadi2=user.kullaniciadi.toString()
                }
            }
            for (begeni in BegenilerList) {

                if (begeni.postid.toString() == post.id) {
                    begenisayaci = begenisayaci + 1
                    if (begeni.begenenkisiid.toString() == auth.currentUser!!.uid.toString()) {
                        likeid = begeni.id.toString()
                    }
                }
            }
            if (KaydedilenPostlar.contains(post)) {
                binding.kaydetbtn.setImageResource(R.drawable.kaydedildi)
            }
            if (BegenilenPostlarList.contains(post)) {
                binding.begenbtn.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            binding.begenbtn.setOnClickListener {
                if (BegenilenPostlarList.contains(post))
                {
                    BegenilenPostlarList.remove(post)
                    PostBegenKaldir(likeid)
                    binding.begenbtn.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    binding.begenisayisi.text=begenisayaci .toString() + " beğenme"
                }
                else{
                    PostBegen(post.id.toString(),post.kimeait.toString(),kullaniciadi2)
                    binding.begenbtn.setImageResource(R.drawable.ic_baseline_favorite_24)
                    BegenilenPostlarList.add(post)
                    binding.begenisayisi.text=(begenisayaci+1).toString()+ " beğenme"
                    val current= LocalDateTime.now()
                    val formatter= DateTimeFormatter.ofPattern("dd MMMM HH:mm", Locale("tr"))
                    val formatted=current.format(formatter)
                    BildirimUpload(post.kimeait.toString(),formatted)
                    UserBegenenlerList.clear()
                }
            }
            binding.kaydetbtn.setOnClickListener {
                if (KaydedilenPostlar.contains(post)) {
                    binding.kaydetbtn.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                    for (spost in TumKaydedilenPostlar) {
                        if (spost.kaydedilenpostid.toString() == post.id.toString()) {
                            spostid = spost.id.toString()
                        }
                    }
                    KaydedilenlerdenKaldir(spostid.toString())
                } else {
                    UploadSavePost(post.id.toString())
                    binding.kaydetbtn.setImageResource(R.drawable.kaydedildi)
                }
            }
            binding.yorumpaylas.setOnClickListener {
                val current= LocalDateTime.now()
                val formatter= DateTimeFormatter.ofPattern("dd MMMM HH:mm", Locale("tr"))
                val formatted=current.format(formatter)
                CommentUpload(post.id.toString(),post.kimeait.toString(),binding.yorumcontent.text.toString(),formatted,kullaniciadi2)
                binding.yorumcontent.text.clear()
                val current2= LocalDateTime.now()
                val formatter2= DateTimeFormatter.ofPattern("dd MMMM HH:mm", Locale("tr"))
                val formatted2=current2.format(formatter2)
                YorumBildirimUpload(post.kimeait.toString(),formatted2)
            }
            binding.yorumlarGorbtn.setOnClickListener {
                yorumdetailclick.onclick(post)
            }
            binding.begenisayisi.setOnClickListener {
                begenisayisiclick.onClick(post)
            }
            for (user in KullanicilarArrayList) {
                if (post.kimeait.toString() == user.id) {
                    binding.kullaniciadi.setOnClickListener {
                        userdetailclick.Onclick(user)
                    }
                    binding.postsahibi.setOnClickListener {
                        userdetailclick.Onclick(user)
                    }
                    binding.kullanicifoto.setOnClickListener {
                        userdetailclick.Onclick(user)
                    }
                    kullaniciadi = user.kullaniciadi.toString()
                    imageurl = user.ImageUrl.toString()
                    Picasso.get().load(post.ImageUrl).into(binding.postfoto)
                    binding.begenisayisi.text = begenisayaci.toString() + " beğenme"
                    Picasso.get().load(suankikullanicilist[0].ImageUrl).into(binding.postuserfoto)
                    Picasso.get().load(imageurl).into(binding.kullanicifoto)
                    binding.kullaniciadi.text = kullaniciadi.toString()
                    binding.postsahibi.text = kullaniciadi.toString()
                    binding.urunaciklama.text = post.aciklamasi.toString()
                    binding.urunfiyati.text = post.urunfiyat.toString() + " TL"
                    binding.urunadi.text = post.urunadi.toString()
                    binding.urunmagazasi.text = post.magaza.toString()
                    binding.urunalNmatarihi.text =
                        post.alinmatarihi.toString()
                    binding.puan.text=post.puan.toString()
                    binding.hangiplatform.text=post.platform.toString()
                    binding.tarih.text=post.postpaylasilmatarihi.toString()

                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = PostrcyclerviewBinding.inflate(from, parent, false)
        return AnasayfaAdapter.CardViewHolder(binding, userdetailclick,yorumdetailclick,begenisayisiclick)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindpost(Postlar[position])
    }

    override fun getItemCount(): Int = Postlar.size

}
private fun UploadSavePost(kaydedilenpostid: String) {
    val uuid1 = UUID.randomUUID()
    val Postmap = hashMapOf<String, Any>()
    Postmap.put("id", uuid1.toString())
    Postmap.put("kaydedenkisiid", auth.currentUser!!.uid.toString())
    Postmap.put("kaydedilenpostid", kaydedilenpostid.toString())
    database.child(uuid1.toString()).setValue(Postmap)
        .addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {

            }
        }.addOnFailureListener { exception ->

        }
}
private fun KaydedilenlerdenKaldir(id: String) {
    database.child(id).removeValue()
        .addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
                //back
            }
        }.addOnFailureListener { exception ->

        }
}
private fun CommentUpload(postId: String, postsahibiid: String,yorum:String,tarih:String,kullaniciadi: String) {
    val uuid1 = UUID.randomUUID()
    val NotificationMap = hashMapOf<String, Any>()
    NotificationMap.put("id", uuid1.toString())
    NotificationMap.put("yorum",yorum.toString())
    NotificationMap.put("yorumyapankisiid", auth.currentUser!!.uid.toString())
    NotificationMap.put("postsahibikisiid",postsahibiid.toString() )
    NotificationMap.put("postid",postId.toString())
    NotificationMap.put("yorumatilmatarihi",tarih.toString())
    database4.child(uuid1.toString()).setValue(NotificationMap)
        .addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
                topic = "/topics/$postsahibiid"
                PushNotification(NotificationData( kullaniciadi.toString(),"Fotoğrafına Yorum Yaptı."),
                    topic).also {
                    sendNotification(it)
                }
            }
        }.addOnFailureListener { exception ->
        }
}
private fun YorumBildirimUpload(id: String,tarih:String) {
    val uuid1 = UUID.randomUUID()
    val NotificationMap = hashMapOf<String, Any>()
    NotificationMap.put("id", uuid1.toString())
    NotificationMap.put("olaynum", "3")
    NotificationMap.put("bildirimsahibi", id)
    NotificationMap.put("olayiyapankisi", auth.currentUser!!.uid.toString())
    NotificationMap.put("olayolmatarihi", tarih.toString())
    database3.child(uuid1.toString()).setValue(NotificationMap)
        .addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
                //back

            }
        }.addOnFailureListener { exception ->

        }
}
private fun PostBegen(postId: String, postsahibiid: String,kullaniciadi:String) {
    val uuid1 = UUID.randomUUID()
    val Postmap = hashMapOf<String, Any>()
    Postmap.put("id", uuid1.toString())
    Postmap.put("begenenkisiid", auth.currentUser!!.uid.toString())
    Postmap.put("postsahibikisiid", postsahibiid.toString())
    Postmap.put("postid", postId.toString())
    database2.child(uuid1.toString()).setValue(Postmap)
        .addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
                topic = "/topics/$postsahibiid"
                PushNotification(
                    NotificationData( kullaniciadi.toString(),"Fotoğrafını Beğendi."),
                    topic).also {
                    sendNotification(it)
                }
            }
        }.addOnFailureListener { exception ->

        }
}
private fun PostBegenKaldir(id: String) {
    database2.child(id).removeValue()
        .addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {

            }
        }.addOnFailureListener { exception ->

        }

}
private fun BildirimUpload(id: String,tarih:String) {
    val uuid1 = UUID.randomUUID()
    val NotificationMap = hashMapOf<String, Any>()
    NotificationMap.put("id", uuid1.toString())
    NotificationMap.put("olaynum", "2")
    NotificationMap.put("bildirimsahibi", id)
    NotificationMap.put("olayiyapankisi", auth.currentUser!!.uid.toString())
    NotificationMap.put("olayolmatarihi", tarih.toString())
    database3.child(uuid1.toString()).setValue(NotificationMap)
        .addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
                //back
            }
        }.addOnFailureListener { exception ->

        }
}
private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
    try {
        val response = RetrofitInstance.api.postNotification(notification)
        if(response.isSuccessful) {
            Log.d("TAG", "Response: ${Gson().toJson(response)}")
        } else {
            Log.e("TAG", response.errorBody()!!.string())
        }
    } catch(e: Exception) {
        Log.e("TAG", e.toString())
    }
}
