package com.muhammetcakir.benimurunum

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.muhammetcakir.benimurunum.Adapter.MessageAdapter
import com.muhammetcakir.benimurunum.Adapter.ProfilePostsAdapter
import com.muhammetcakir.benimurunum.Adapter.database3
import com.muhammetcakir.benimurunum.ClickListeners.PostClick
import com.muhammetcakir.benimurunum.Models.Follow
import com.muhammetcakir.benimurunum.Models.Message
import com.muhammetcakir.benimurunum.Models.MyFuncs
import com.muhammetcakir.benimurunum.Models.Post
import com.muhammetcakir.benimurunum.databinding.ActivityProfileBinding
import com.squareup.picasso.Picasso


class Profile : AppCompatActivity(), PostClick {
    private lateinit var binding: ActivityProfileBinding
    val db = FirebaseDatabase.getInstance().getReference("Follows")
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var fonksiyonlar = MyFuncs()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
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
        SeciliPostArrayList.clear()
        for (post in PostlarArrayList) {
            if (post.kimeait.toString() == auth.currentUser!!.uid.toString()) {
                if (!SeciliPostArrayList.contains(post)) {
                    SeciliPostArrayList.add(post)
                }
            }
        }
        tekrarkisim()
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
        binding.rc.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = ProfilePostsAdapter(SeciliPostArrayList, this@Profile)
        }
        binding.settingsbutton.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }
        binding.takipcilayout.setOnClickListener {
            startActivity(Intent(this, TakipcilerPage::class.java))
        }
        binding.takipedilenlayout.setOnClickListener {
            startActivity(Intent(this, TakipEdilenlerPage::class.java))
        }
        binding.kaydedilenlerbutton.setOnClickListener {
            startActivity(Intent(this, SavedPostsPage::class.java))
        }
        db.addValueEventListener(object : ChildEventListener, ValueEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                yenile()
            }
            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                yenile()
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                yenile()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                yenile()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener {
            SeciliPostArrayList.clear()
            for (post in PostlarArrayList) {
                if (post.kimeait.toString() == auth.currentUser!!.uid.toString()) {
                    if (!SeciliPostArrayList.contains(post)) {
                        SeciliPostArrayList.add(post)
                    }
                }
            }
            binding.rc.apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = ProfilePostsAdapter(SeciliPostArrayList, this@Profile)
            }
            tekrarkisim()
            swipeRefreshLayout!!.isRefreshing = false
        }
    }

    override fun onClick(post: Post) {
        val intent = Intent(applicationContext, PostDetail::class.java)
        intent.putExtra(POST_ID_EXTRA, post.id.toString())
        startActivity(intent)
    }
    private fun yenile() {
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
        binding.takipcisayisi.text = TakipcilerimList.count().toString()
        binding.takipedilensayisi.text = TakipEttiklerimList.count().toString()
    }
    private fun tekrarkisim() {
        if (suankikullanicilist.isNotEmpty()) {
            Picasso.get().load(suankikullanicilist[0].ImageUrl).into(binding.userphoto)
            binding.profilkullaniciadi.setText(suankikullanicilist[0].kullaniciadi)
            binding.biyografi.setText(suankikullanicilist[0].isim)
            binding.postsayisi.text = SeciliPostArrayList.count().toString()
            binding.takipcisayisi.text = TakipcilerimList.count().toString()
            binding.takipedilensayisi.text = TakipEttiklerimList.count().toString()
        } else {
            Picasso.get().load(yenikikullanicilist[0].ImageUrl).into(binding.userphoto)
            binding.profilkullaniciadi.setText(yenikikullanicilist[0].kullaniciadi)
            binding.biyografi.setText(yenikikullanicilist[0].isim)
            binding.postsayisi.text = SeciliPostArrayList.count().toString()
            binding.takipcisayisi.text = TakipcilerimList.count().toString()
            binding.takipedilensayisi.text = TakipEttiklerimList.count().toString()
        }
    }
}