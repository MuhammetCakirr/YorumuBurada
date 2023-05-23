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
import com.muhammetcakir.benimurunum.Adapter.NotificationAdapter
import com.muhammetcakir.benimurunum.Adapter.ProfilePostsAdapter
import com.muhammetcakir.benimurunum.Adapter.SearchAdapter
import com.muhammetcakir.benimurunum.ClickListeners.PostClick
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.*
import com.muhammetcakir.benimurunum.databinding.ActivityNotificationPageBinding

class NotificationPage : AppCompatActivity(), PostClick, UserSearchClick {
    private lateinit var binding: ActivityNotificationPageBinding
    var fonksiyonlar = MyFuncs()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    val db = FirebaseDatabase.getInstance().getReference("Notification")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationPageBinding.inflate(layoutInflater)
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
        binding.backlayout.setOnClickListener {
            startActivity(Intent(this, MainPage::class.java))
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
        rcyenile()
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener {
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    BildirimlerList.clear()
                    if (snapshot.exists()) {
                        for (followSnapshot in snapshot.children) {
                            var follow = followSnapshot.getValue(Notification::class.java)
                            BildirimlerList.add(follow!!)
                        }
                    }
                    rcyenile()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            rcyenile()
            swipeRefreshLayout!!.isRefreshing = false
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

    override fun onClick(post: Post) {
        val intent = Intent(applicationContext, PostDetail::class.java)
        intent.putExtra(POST_ID_EXTRA, post.id.toString())
        startActivity(intent)
    }

    override fun Onclick(user: User) {
        val intent = Intent(applicationContext, UserProfile::class.java)
        intent.putExtra(USER_ID_EXTRA, user.id.toString())
        startActivity(intent)
    }

    private fun rcyenile() {
        BenimBildirimlerList.clear()
        for (bildirim in BildirimlerList) {
            if (bildirim.bildirimsahibi.toString() == auth.currentUser!!.uid.toString()) {
                BenimBildirimlerList.add(bildirim)
            }
        }
        binding.notificationrc.apply {
            layoutManager = LinearLayoutManager(
                this@NotificationPage,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = NotificationAdapter(
                BenimBildirimlerList,
                this@NotificationPage,
                this@NotificationPage
            )
            binding.notificationrc.adapter = adapter
            binding.notificationrc.layoutManager = layoutManager
        }
    }
}