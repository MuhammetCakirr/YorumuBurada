package com.muhammetcakir.benimurunum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muhammetcakir.benimurunum.Adapter.AnasayfaAdapter
import com.muhammetcakir.benimurunum.Adapter.CommentAdapter
import com.muhammetcakir.benimurunum.Adapter.ProfilePostsAdapter
import com.muhammetcakir.benimurunum.ClickListeners.CommentClick
import com.muhammetcakir.benimurunum.ClickListeners.NumberOfLike
import com.muhammetcakir.benimurunum.ClickListeners.PostClick
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.*
import com.muhammetcakir.benimurunum.databinding.ActivityMainPageBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class MainPage : AppCompatActivity(), UserSearchClick, CommentClick, NumberOfLike {
    private lateinit var binding: ActivityMainPageBinding
    var fonksiyonlar = MyFuncs()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    val db = FirebaseDatabase.getInstance().getReference("Posts")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        runBlocking {
            async {
                fonksiyonlar.Getkullanicilar()
                fonksiyonlar.GetPostlar()
                fonksiyonlar.GetTakipler()
                fonksiyonlar.GetBegeniler()
                fonksiyonlar.GetBildirimler()
                fonksiyonlar.GetKaydedilenPostlar()
                fonksiyonlar.GetMesajlar()
                fonksiyonlar.GetYorumlar()
                if (PostlarArrayList.isNotEmpty()) {
                    AnasayfaPostlar.clear()
                    for (post in PostlarArrayList) {
                        for (takipettiklerim in TakipEttiklerimList) {
                            if (takipettiklerim.id.toString() == post.kimeait.toString()) {
                                AnasayfaPostlar.add(post)
                            }
                        }
                    }
                } else {
                    AnasayfaPostlar.clear()
                    for (post in PostlarArrayList) {
                        for (takipettiklerim in TakipEttiklerimList) {
                            if (takipettiklerim.id.toString() == post.kimeait.toString()) {
                                AnasayfaPostlar.add(post)
                            }
                        }
                    }
                }
                binding.anasayfarc.apply {
                    layoutManager = LinearLayoutManager(
                        this@MainPage,
                        LinearLayoutManager.VERTICAL, false
                    )
                    adapter = AnasayfaAdapter(
                        AnasayfaPostlar,
                        this@MainPage,
                        this@MainPage,
                        this@MainPage
                    )
                    binding.anasayfarc.adapter = adapter
                    binding.anasayfarc.layoutManager = layoutManager
                }
            }
        }
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener {
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    followArrayList.clear()
                    if (snapshot.exists()) {
                        for (followSnapshot in snapshot.children) {
                            var follow = followSnapshot.getValue(Follow::class.java)
                            followArrayList.add(follow!!)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            AnasayfaPostlar.clear()
            for (post in PostlarArrayList) {
                for (takipettiklerim in TakipEttiklerimList) {
                    if (takipettiklerim.id.toString() == post.kimeait.toString()) {
                        AnasayfaPostlar.add(post)
                    }
                }
            }
            binding.anasayfarc.apply {
                layoutManager = LinearLayoutManager(
                    this@MainPage,
                    LinearLayoutManager.VERTICAL, false
                )
                adapter =
                    AnasayfaAdapter(AnasayfaPostlar, this@MainPage, this@MainPage, this@MainPage)
                binding.anasayfarc.adapter = adapter
                binding.anasayfarc.layoutManager = layoutManager
            }

            swipeRefreshLayout!!.isRefreshing = false
        }

        BegenilenPostlarList.clear()
        for (begeni in BegenilerList) {
            for (post in PostlarArrayList) {
                if (begeni.postid.toString() == post.id && begeni.begenenkisiid.toString() == auth.currentUser!!.uid.toString()) {
                    BegenilenPostlarList.add(post)
                }
            }
        }
        KaydedilenPostlar.clear()
        for (savepost in TumKaydedilenPostlar) {
            if (savepost.kaydedenkisiid == auth.currentUser!!.uid.toString()) {
                for (post in PostlarArrayList) {
                    if (post.id.toString() == savepost.kaydedilenpostid.toString()) {
                        KaydedilenPostlar.add(post)
                    }
                }
            }
        }
        TakipEttiklerimList.clear()
        for (follow in followArrayList) {
            if (follow.takipedenkisiid == auth.currentUser!!.uid.toString()) {
                for (user in KullanicilarArrayList) {
                    if (follow.takipedilenkisiid == user.id.toString()) {
                        TakipEttiklerimList.add(user)
                    }
                }
            }
        }
        TakipcilerimList.clear()
        for (follow in followArrayList) {
            if (follow.takipedilenkisiid == auth.currentUser!!.uid.toString()) {
                for (user in KullanicilarArrayList) {
                    if (follow.takipedenkisiid == user.id.toString()) {
                        TakipcilerimList.add(user)
                    }
                }
            }
        }
        binding.bildirimbutton.setOnClickListener {
            startActivity(Intent(this, NotificationPage::class.java))
        }
        binding.mesajlarbutton.setOnClickListener {
            startActivity(Intent(this, ChooseMessagePage::class.java))
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.anasayfa -> startActivity(Intent(this, MainPage::class.java))
                R.id.olustur -> startActivity(Intent(this, AddProduct::class.java))
                R.id.profilim -> startActivity(Intent(this, Profile::class.java))
                R.id.ara -> startActivity(Intent(this, SearchPage::class.java))
                else -> {
                }
            }
            true
        }
    }

    override fun Onclick(user: User) { //Post sahibine gidiyor
        val intent = Intent(applicationContext, UserProfile::class.java)
        intent.putExtra(USER_ID_EXTRA, user.id.toString())
        startActivity(intent)
    }

    override fun onclick(post: Post) { //Yorum Sayfasına gidiyor
        val intent = Intent(applicationContext, CommentPage::class.java)
        intent.putExtra(POST_ID_EXTRA, post.id.toString())
        startActivity(intent)
    }

    override fun onClick(post: Post) { //Yorum Sayfasına gidiyor
        val intent = Intent(applicationContext, ListofLike::class.java)
        intent.putExtra(POST_ID_EXTRA, post.id.toString())
        startActivity(intent)
    }
}