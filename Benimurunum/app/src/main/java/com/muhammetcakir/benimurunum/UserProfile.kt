package com.muhammetcakir.benimurunum

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.muhammetcakir.benimurunum.Adapter.ProfilePostsAdapter
import com.muhammetcakir.benimurunum.ClickListeners.PostClick
import com.muhammetcakir.benimurunum.Models.*
import com.muhammetcakir.benimurunum.databinding.ActivityUserProfileBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class UserProfile : AppCompatActivity(), PostClick {
    var idler: String = ""
    private lateinit var binding: ActivityUserProfileBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseDatabase.getInstance().reference
    var fonksiyonlar= MyFuncs()
    var topic = ""
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
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
        var sayac: Int = 0
        var kullaniciadi2:String=""
        var followid: String = ""
        val userId = intent.getStringExtra(USER_ID_EXTRA)
        val user = userFromID(userId.toString())
        if (user != null) {
            SearchTakipEttikleriList.clear()
            for (follow in followArrayList) {
                if (follow.takipedenkisiid == user.id.toString()) {
                    for (a in KullanicilarArrayList) {
                        if (follow.takipedilenkisiid == a.id.toString()) {
                            SearchTakipEttikleriList.add(a)
                        }
                    }
                }
            }
            SearchTakipcileriList.clear()
            for (follow in followArrayList) {
                if (follow.takipedilenkisiid == user.id.toString()) {
                    for (b in KullanicilarArrayList) {
                        if (follow.takipedenkisiid == b.id.toString()) {
                            SearchTakipcileriList.add(b)
                        }
                    }
                }
            }
            SearchUserPostArrayList.clear()
            for (post in PostlarArrayList) {
                if (post.kimeait.toString() == user.id.toString()) {
                    sayac = sayac + 1
                    SearchUserPostArrayList.add(post)
                }
            }
            binding.profilkullaniciadi.text = user.kullaniciadi
            Picasso.get().load(user.ImageUrl).into(binding.userphoto)
            binding.biyografi.text = user.isim
            binding.takipci.text = SearchTakipcileriList.count().toString()
            binding.takipedilen.text = SearchTakipEttikleriList.count().toString()
            binding.postsayisi.text = sayac.toString()

            for (kisi in TakipEttiklerimList) {
                if (kisi.id.toString() == user.id.toString()) {
                    binding.TakipetBtn.text = "Takipten Çık"
                }
            }
            for (follow in followArrayList) {
                if (follow.takipedenkisiid == auth.currentUser!!.uid.toString() && follow.takipedilenkisiid == user.id.toString()) {
                    followid = follow.id.toString()
                }
            }
            binding.TakipetBtn.setOnClickListener {
                if (!TakipEttiklerimList.contains(user)) {
                    val current2= LocalDateTime.now()
                    val formatter2= DateTimeFormatter.ofPattern("dd MMMM ", Locale("tr"))
                    val formatted2=current2.format(formatter2)
                    TakipIdUpload(user.id.toString(),kullaniciadi2.toString())
                    BildirimUpload(user.id.toString(),formatted2)
                    binding.TakipetBtn.text = "Takipten Çık"
                    TakipEttiklerimList.add(user)
                    SearchTakipEttikleriList.clear()
                    for (follow in followArrayList) {
                        if (follow.takipedenkisiid == user!!.id.toString()) {
                            for (a in KullanicilarArrayList) {
                                if (follow.takipedilenkisiid == a.id.toString()) {
                                    SearchTakipEttikleriList.add(a)
                                }
                            }
                        }
                    }
                    binding.takipci.text = SearchTakipcileriList.count().toString()

                } else {
                    TakiptenCik(followid.toString(), user.id.toString())
                    binding.TakipetBtn.text = "Takip Et"
                    followid = ""
                    TakipEttiklerimList.remove(user)
                    SearchTakipcileriList.clear()
                    for (follow in followArrayList) {
                        if (follow.takipedilenkisiid == user!!.id.toString()) {
                            for (b in KullanicilarArrayList) {
                                if (follow.takipedenkisiid == b.id.toString()) {
                                    SearchTakipcileriList.add(b)
                                }
                            }
                        }
                    }
                    binding.takipci.text = (SearchTakipcileriList.count()-1).toString()
                }
            }
            db.child("Follows").addValueEventListener(object : ChildEventListener, ValueEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        followArrayList.clear()
                        for (notSnapshot in snapshot.children) {
                            var follow = notSnapshot.getValue(Follow::class.java)
                            followArrayList.add(follow!!)
                        }
                        SearchTakipcileriList.clear()
                        for (follow in followArrayList) {
                            if (follow.takipedilenkisiid == user!!.id.toString()) {
                                for (b in KullanicilarArrayList) {
                                    if (follow.takipedenkisiid == b.id.toString()) {
                                        SearchTakipcileriList.add(b)
                                    }
                                }
                            }
                        }
                        binding.takipci.text = SearchTakipcileriList.count().toString()

                    }
                }
                override fun onChildChanged(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    if (snapshot.exists()) {
                        followArrayList.clear()
                        for (notSnapshot in snapshot.children) {
                            var follow = notSnapshot.getValue(Follow::class.java)
                            followArrayList.add(follow!!)
                        }
                        SearchTakipcileriList.clear()
                        for (follow in followArrayList) {
                            if (follow.takipedilenkisiid == user!!.id.toString()) {
                                for (b in KullanicilarArrayList) {
                                    if (follow.takipedenkisiid == b.id.toString()) {
                                        SearchTakipcileriList.add(b)
                                    }
                                }
                            }
                        }
                        binding.takipci.text = SearchTakipcileriList.count().toString()
                    }
                }
                override fun onChildRemoved(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        followArrayList.clear()
                        for (notSnapshot in snapshot.children) {
                            var follow = notSnapshot.getValue(Follow::class.java)
                            followArrayList.add(follow!!)
                        }
                        SearchTakipcileriList.clear()
                        for (follow in followArrayList) {
                            if (follow.takipedilenkisiid == user!!.id.toString()) {
                                for (b in KullanicilarArrayList) {
                                    if (follow.takipedenkisiid == b.id.toString()) {
                                        SearchTakipcileriList.add(b)
                                    }
                                }
                            }
                        }
                        binding.takipci.text = SearchTakipcileriList.count().toString()
                    }
                }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        followArrayList.clear()
                        for (notSnapshot in snapshot.children) {
                            var follow = notSnapshot.getValue(Follow::class.java)
                            followArrayList.add(follow!!)
                        }
                        SearchTakipcileriList.clear()
                        for (follow in followArrayList) {
                            if (follow.takipedilenkisiid == user!!.id.toString()) {
                                for (b in KullanicilarArrayList) {
                                    if (follow.takipedenkisiid == b.id.toString()) {
                                        SearchTakipcileriList.add(b)
                                    }
                                }
                            }
                        }
                        binding.takipci.text = SearchTakipcileriList.count().toString()
                    }
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        followArrayList.clear()
                        for (notSnapshot in snapshot.children) {
                            var follow = notSnapshot.getValue(Follow::class.java)
                            followArrayList.add(follow!!)
                        }
                        SearchTakipcileriList.clear()
                        for (follow in followArrayList) {
                            if (follow.takipedilenkisiid == user!!.id.toString()) {
                                for (b in KullanicilarArrayList) {
                                    if (follow.takipedenkisiid == b.id.toString()) {
                                        SearchTakipcileriList.add(b)
                                    }
                                }
                            }
                        }
                        binding.takipci.text = SearchTakipcileriList.count().toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        binding.rc.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = ProfilePostsAdapter(SearchUserPostArrayList, this@UserProfile)
        }
        for (user in KullanicilarArrayList)
        {
            if(user.id.toString()==auth.currentUser!!.uid.toString())
            {
                kullaniciadi2=user.kullaniciadi.toString()
            }
        }
        binding.takipedilenlayout.setOnClickListener {
            val intent = Intent(this, UserTakipEdilenler::class.java)
            startActivity(intent)
        }
        binding.takipcilayout.setOnClickListener {
            val intent = Intent(this, UserTakipciler::class.java)
            startActivity(intent)
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.anasayfa ->startActivity(Intent(this, MainPage::class.java))
                R.id.olustur ->startActivity(Intent(this, AddProduct::class.java))
                R.id.profilim ->startActivity(Intent(this, Profile::class.java))
                R.id.ara ->startActivity(Intent(this, SearchPage::class.java))
                else -> {
                }
            }
            true
        }


    }
    private fun TakipIdUpload(id: String,kullaniciadi:String) {
        val uuid1 = UUID.randomUUID()
        val FollowMap = hashMapOf<String, Any>()
        FollowMap.put("id", uuid1.toString())
        FollowMap.put("takipedenkisiid", auth.currentUser!!.uid.toString())
        FollowMap.put("takipedilenkisiid", id)
        db.child("Follows").child(uuid1.toString()).setValue(FollowMap)
            .addOnCompleteListener { task ->
                if (task.isComplete && task.isSuccessful) {
                    topic = "/topics/$id"
                    PushNotification(NotificationData( kullaniciadi.toString(),"Seni Takip Etti."),
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
    private fun BildirimUpload(id: String,tarih:String) {
        val uuid1 = UUID.randomUUID()
        val NotificationMap = hashMapOf<String, Any>()
        NotificationMap.put("id", uuid1.toString())
        NotificationMap.put("olaynum", "1")
        NotificationMap.put("bildirimsahibi", id)
        NotificationMap.put("olayiyapankisi", auth.currentUser!!.uid.toString())
        NotificationMap.put("olayolmatarihi",tarih.toString())

        db.child("Notification").child(uuid1.toString()).setValue(NotificationMap)
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
    private fun TakiptenCik(id: String, id2: String) {
        db.child("Follows").child(id).removeValue()
            .addOnCompleteListener { task ->
                if (task.isComplete && task.isSuccessful) {
                    //back
                    Toast.makeText(getApplicationContext(), "Takipten Çıkıldı", Toast.LENGTH_LONG)
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
    private fun userFromID(userId: String): User? {
        for (user in KullanicilarArrayList) {
            if (user.id.toString() == userId)
                return user
        }
        return null
    }
    override fun onClick(post: Post) {
        val intent = Intent(applicationContext, PostDetail::class.java)
        intent.putExtra(POST_ID_EXTRA, post.id.toString())
        startActivity(intent)
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
}