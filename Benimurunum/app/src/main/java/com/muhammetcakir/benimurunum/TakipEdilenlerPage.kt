package com.muhammetcakir.benimurunum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.muhammetcakir.benimurunum.Adapter.FollowAdapter
import com.muhammetcakir.benimurunum.Adapter.SearchAdapter
import com.muhammetcakir.benimurunum.Adapter.SearchUserFollowAdapter
import com.muhammetcakir.benimurunum.Adapter.TakipEdilenlerAdapter
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.Follow
import com.muhammetcakir.benimurunum.Models.MyFuncs
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.databinding.ActivityTakipEdilenlerPageBinding
import com.muhammetcakir.benimurunum.databinding.ActivityTakipcilerPageBinding

class TakipEdilenlerPage : AppCompatActivity(),UserSearchClick {
    private lateinit var binding: ActivityTakipEdilenlerPageBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseDatabase.getInstance().getReference("Follows")
    var fonksiyonlar= MyFuncs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTakipEdilenlerPageBinding.inflate(layoutInflater)
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
            binding.takipedilenlerrc.apply {
                layoutManager = LinearLayoutManager(this@TakipEdilenlerPage,
                    LinearLayoutManager.VERTICAL,false)
                adapter = TakipEdilenlerAdapter(TakipEttiklerimList,this@TakipEdilenlerPage)
                binding.takipedilenlerrc.adapter = adapter
                binding.takipedilenlerrc.layoutManager=layoutManager
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
        db.addValueEventListener(object : ChildEventListener, ValueEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                kontrol()
            }
            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                kontrol()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                kontrol()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                kontrol()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.backbutton.setOnClickListener {
            startActivity(Intent(this,Profile::class.java))
        }
    }

    override fun Onclick(user: User) {
        val intent = Intent(applicationContext, UserProfile::class.java)
        intent.putExtra(USER_ID_EXTRA, user.id.toString())
        startActivity(intent)
    }
    private fun kontrol()
    {
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
        binding.takipedilenlerrc.apply {
            layoutManager = LinearLayoutManager(this@TakipEdilenlerPage,
                LinearLayoutManager.VERTICAL,false)
            adapter = TakipEdilenlerAdapter(TakipEttiklerimList,this@TakipEdilenlerPage)
            binding.takipedilenlerrc.adapter = adapter
            binding.takipedilenlerrc.layoutManager=layoutManager
        }
    }
}