package com.muhammetcakir.benimurunum

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.muhammetcakir.benimurunum.Models.*
import com.muhammetcakir.benimurunum.databinding.ActivityPostDetailBinding
import com.squareup.picasso.Picasso
import id.ionbit.ionalert.IonAlert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PostDetail : AppCompatActivity() {
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("SavePosts")
    val database2 = FirebaseDatabase.getInstance().getReference("Likes")
    val database3 = FirebaseDatabase.getInstance().getReference("Notification")
    val database4 = FirebaseDatabase.getInstance().getReference("Comments")
    val database5 = FirebaseDatabase.getInstance().getReference("Posts")
    var fonksiyonlar = MyFuncs()
    var topic = ""
    private lateinit var binding: ActivityPostDetailBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        fonksiyonlar.Getkullanicilar()
        fonksiyonlar.GetPostlar()
        fonksiyonlar.GetTakipler()
        fonksiyonlar.GetBegeniler()
        fonksiyonlar.GetBildirimler()
        fonksiyonlar.GetKaydedilenPostlar()
        fonksiyonlar.GetMesajlar()
        fonksiyonlar.GetYorumlar()
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener {
            var getdata6 = database2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    BegenilerList.clear()
                    if (snapshot.exists()) {
                        for (notSnapshot in snapshot.children) {
                            var like = notSnapshot.getValue(Like::class.java)
                            BegenilerList.add(like!!)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
            )
            database2.addListenerForSingleValueEvent(getdata6)
            var spostid: String = ""
            var likeid: String = ""
            var begenisayaci: Int = 0
            val postId = intent.getStringExtra(POST_ID_EXTRA)
            val post = postFromID(postId.toString())
            println(postId)
            if (post != null) {
                BegenilenPostlarList.clear()
                for (begeni in BegenilerList) {
                    for (post in PostlarArrayList) {
                        if (begeni.postid.toString() == post.id && begeni.begenenkisiid.toString() == auth.currentUser!!.uid.toString()) {
                            BegenilenPostlarList.add(post)
                        }
                    }
                }
                UserBegenenlerList.clear()
                for (begenme in BegenilerList) {
                    for (user in KullanicilarArrayList) {
                        if (user.id.toString() == begenme.begenenkisiid.toString() && post.id.toString() == begenme.postid.toString()) {
                            UserBegenenlerList.add(user)
                        }
                    }
                }
                var fotoid = post.kimeait
                Postsahibi.clear()
                for (user in KullanicilarArrayList) {
                    if (fotoid.toString() == user.id.toString()) {
                        Postsahibi.add(user)
                    }
                }
                if (KaydedilenPostlar.contains(post)) {
                    binding.kaydetbtn.setImageResource(R.drawable.kaydedildi)
                }
                for (begeni in BegenilerList) {

                    if (begeni.postid.toString() == post.id) {
                        begenisayaci = begenisayaci + 1
                        if (begeni.begenenkisiid.toString() == auth.currentUser!!.uid.toString()) {
                            likeid = begeni.id.toString()
                            binding.begenbtn.setImageResource(R.drawable.ic_baseline_favorite_24)
                        }
                    }
                }
                Picasso.get().load(post.ImageUrl).into(binding.postfoto)
                binding.postsahibi.text = Postsahibi[0].kullaniciadi
                Picasso.get().load(Postsahibi[0].ImageUrl).into(binding.kullanicifoto)
                binding.kullaniciadi.text = Postsahibi[0].kullaniciadi
                Picasso.get().load(suankikullanicilist[0].ImageUrl).into(binding.postuserfoto)
                binding.urunaciklama.text = post.aciklamasi
                binding.urunadi.text = post.urunadi
                binding.urunfiyati.text = post.urunfiyat + " TL"
                binding.urunmagazasi.text = post.magaza
                binding.urunalNmatarihi.text = post.alinmatarihi
                binding.begenisayisi.text = begenisayaci.toString() + " beğenme"
                if (post.indirimdemi == "1") {
                    binding.indirimdemi2.text = "Evet"
                } else {
                    binding.indirimdemi2.text = "Hayır"
                }
                if (post.onlinemi == "1") {
                    binding.onlinemi2.text = "Evet"
                } else {
                    binding.onlinemi2.text = "Hayır"
                }
            }
            swipeRefreshLayout!!.isRefreshing = false
        }
        var kullaniciadi2:String=""
        for (user in KullanicilarArrayList)
        {
            if(user.id.toString()==auth.currentUser!!.uid.toString())
            {
                kullaniciadi2=user.kullaniciadi.toString()
            }
        }
        var spostid: String = ""
        var likeid: String = ""
        var begenisayaci: Int = 0
        val postId = intent.getStringExtra(POST_ID_EXTRA)
        val post = postFromID(postId.toString())
        println(postId)
        if (post != null) {
            if (post.kimeait != auth.currentUser!!.uid.toString()) {
                binding.postsil.visibility = View.GONE
                binding.postduzenle.visibility = View.GONE
            }
            BegenilenPostlarList.clear()
            for (begeni in BegenilerList) {
                for (post in PostlarArrayList) {
                    if (begeni.postid.toString() == post.id && begeni.begenenkisiid.toString() == auth.currentUser!!.uid.toString()) {
                        BegenilenPostlarList.add(post)
                    }
                }
            }
            UserBegenenlerList.clear()
            for (begenme in BegenilerList) {
                for (user in KullanicilarArrayList) {
                    if (user.id.toString() == begenme.begenenkisiid.toString() && post.id.toString() == begenme.postid.toString()) {
                        UserBegenenlerList.add(user)
                    }
                }
            }
            var fotoid = post.kimeait
            Postsahibi.clear()
            for (user in KullanicilarArrayList) {
                if (fotoid.toString() == user.id.toString()) {
                    Postsahibi.add(user)
                }
            }
            if (KaydedilenPostlar.contains(post)) {
                binding.kaydetbtn.setImageResource(R.drawable.kaydedildi)
            }
            for (begeni in BegenilerList) {

                if (begeni.postid.toString() == post.id) {
                    begenisayaci = begenisayaci + 1
                    if (begeni.begenenkisiid.toString() == auth.currentUser!!.uid.toString()) {
                        likeid = begeni.id.toString()
                        binding.begenbtn.setImageResource(R.drawable.ic_baseline_favorite_24)
                    }
                }
            }
            Picasso.get().load(post.ImageUrl).into(binding.postfoto)
            binding.postsahibi.text = Postsahibi[0].kullaniciadi
            Picasso.get().load(Postsahibi[0].ImageUrl).into(binding.kullanicifoto)
            binding.kullaniciadi.text = Postsahibi[0].kullaniciadi
            Picasso.get().load(Postsahibi[0].ImageUrl).into(binding.postuserfoto)
            binding.urunaciklama.text = post.aciklamasi
            binding.urunadi.text = post.urunadi
            binding.urunfiyati.text = post.urunfiyat + " TL"
            binding.urunmagazasi.text = post.magaza
            binding.urunalNmatarihi.text = post.alinmatarihi
            binding.begenisayisi.text = begenisayaci.toString() + " beğenme"
            binding.puan.text = post.puan.toString()
            binding.hangiplatform.text = post.platform.toString()
            binding.tarih.text = post.postpaylasilmatarihi.toString()
            if (post.indirimdemi == "1") {
                binding.indirimdemi.visibility = View.VISIBLE
            }
            if (post.onlinemi == "1") {
                binding.onlinemi.visibility = View.VISIBLE
            }
            binding.begenbtn.setOnClickListener {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd MMMM HH:mm", Locale("tr"))
                val formatted = current.format(formatter)
                if (BegenilenPostlarList.contains(post)) {
                    BegenilenPostlarList.remove(post)
                    PostBegenKaldir(likeid)
                    binding.begenbtn.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    binding.begenisayisi.text = begenisayaci.toString() + " beğenme"
                } else {
                    PostBegen(post.id.toString(), post.kimeait.toString(),kullaniciadi2)
                    binding.begenbtn.setImageResource(R.drawable.ic_baseline_favorite_24)
                    BegenilenPostlarList.add(post)
                    binding.begenisayisi.text = (begenisayaci + 1).toString() + " beğenme"
                    BildirimUpload(post.kimeait.toString(), formatted)
                    UserBegenenlerList.clear()
                }
            }
            binding.postsil.setOnClickListener {
                IonAlert(this, IonAlert.ERROR_TYPE)
                    .setTitleText("Paylaşımını Silmek İstediğine Emin Misin?")
                    .setCancelText("İptal")
                    .setConfirmText("Sil")
                    .setConfirmClickListener(object : IonAlert.ClickListener {
                        override fun onClick(sDialog: IonAlert) {
                            removedatabase(post.id.toString())
                            startActivity(Intent(this@PostDetail, Profile::class.java))
                            sDialog.dismissWithAnimation()
                        }
                    })
                    .setCancelClickListener(object : IonAlert.ClickListener {
                        override fun onClick(sDialog: IonAlert) {
                            sDialog.dismissWithAnimation()
                        }
                    })
                    .show()
            }
            binding.kaydetbtn.setOnClickListener {
                if (KaydedilenPostlar.contains(post)) {
                    for (spost in TumKaydedilenPostlar) {
                        if (spost.kaydedilenpostid.toString() == post.id.toString()) {
                            spostid = spost.id.toString()
                        }
                    }
                    KaydedilenPostlar.remove(post)
                    binding.kaydetbtn.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                    KaydedilenlerdenKaldir(spostid)
                } else {
                    KaydedilenPostlar.add(post)
                    binding.kaydetbtn.setImageResource(R.drawable.kaydedildi)
                    UploadSavePost(post.id.toString())
                }
            }
            binding.begenisayisi.setOnClickListener {
                val intent = Intent(applicationContext, ListofLike::class.java)
                intent.putExtra(POST_ID_EXTRA, post.id.toString())
                startActivity(intent)
            }
            binding.yorumpaylas.setOnClickListener {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd MMMM HH:mm", Locale("tr"))
                val formatted = current.format(formatter)
                CommentUpload(post.id.toString(), post.kimeait.toString(), formatted,kullaniciadi2.toString())
                binding.yorumcontent.text.clear()
                YorumBildirimUpload(post.kimeait.toString(), formatted)
            }
            binding.yorumlarGorbtn.setOnClickListener {
                val intent = Intent(applicationContext, CommentPage::class.java)
                intent.putExtra(POST_ID_EXTRA, post.id.toString())
                startActivity(intent)
            }
            binding.postduzenle.setOnClickListener {
                val intent = Intent(applicationContext, EditPost::class.java)
                intent.putExtra(POST_ID_EXTRA, post.id.toString())
                startActivity(intent)
            }
        }
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
                    Toast.makeText(getApplicationContext(), "Kaydedildi", Toast.LENGTH_LONG).show();
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
    private fun KaydedilenlerdenKaldir(id: String) {
        database.child(id).removeValue()
            .addOnCompleteListener { task ->
                if (task.isComplete && task.isSuccessful) {
                    //back
                    Toast.makeText(
                        getApplicationContext(),
                        "Kaydedilenlerden Kaldırıldı",
                        Toast.LENGTH_LONG
                    )
                        .show();
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
    private fun postFromID(postId: String): Post? {
        for (post in PostlarArrayList) {
            if (post.id == postId)
                return post
        }
        return null
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
                    PushNotification(NotificationData( kullaniciadi.toString(),"Fotoğrafını Beğendi."),
                        topic).also {
                        sendNotification(it)
                    }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }

    }
    private fun PostBegenKaldir(id: String) {
        database2.child(id).removeValue()
            .addOnCompleteListener { task ->
                if (task.isComplete && task.isSuccessful) {
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
    private fun BildirimUpload(id: String, tarih: String) {
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
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
    private fun YorumBildirimUpload(id: String, tarih: String) {
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
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
    private fun CommentUpload(postId: String, postsahibiid: String, tarih: String,kullaniciadi: String) {
        val uuid1 = UUID.randomUUID()
        val NotificationMap = hashMapOf<String, Any>()
        NotificationMap.put("id", uuid1.toString())
        NotificationMap.put("yorum", binding.yorumcontent.text.toString())
        NotificationMap.put("yorumyapankisiid", auth.currentUser!!.uid.toString())
        NotificationMap.put("postsahibikisiid", postsahibiid.toString())
        NotificationMap.put("postid", postId.toString())
        NotificationMap.put("yorumatilmatarihi", tarih.toString())
        database4.child(uuid1.toString()).setValue(NotificationMap)
            .addOnCompleteListener { task ->
                if (task.isComplete && task.isSuccessful) {
                    //back
                    Toast.makeText(getApplicationContext(), "Yorum Eklendi", Toast.LENGTH_LONG)
                        .show()
                    topic = "/topics/$postsahibiid"
                    PushNotification(NotificationData( kullaniciadi.toString(),"Fotoğrafına Yorum Yaptı."),
                        topic).also {
                        sendNotification(it)
                    }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
    private fun removedatabase(id: String) {
        database5.child(id).removeValue()
            .addOnCompleteListener { task ->
                if (task.isComplete && task.isSuccessful) {
                    //back
                    Toast.makeText(getApplicationContext(), "Post Silindi.", Toast.LENGTH_LONG)
                        .show();
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.Default).launch {
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
}