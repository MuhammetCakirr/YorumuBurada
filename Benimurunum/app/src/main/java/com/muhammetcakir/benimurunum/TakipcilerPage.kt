package com.muhammetcakir.benimurunum
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.muhammetcakir.benimurunum.Adapter.FollowAdapter
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.Follow
import com.muhammetcakir.benimurunum.Models.MyFuncs
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.databinding.ActivityTakipcilerPageBinding


class TakipcilerPage : AppCompatActivity(),UserSearchClick {
    private lateinit var binding: ActivityTakipcilerPageBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseDatabase.getInstance().getReference("Follows")
    var fonksiyonlar= MyFuncs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTakipcilerPageBinding.inflate(layoutInflater)
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

            binding.takipcirc.apply {
                layoutManager = LinearLayoutManager(this@TakipcilerPage,
                    LinearLayoutManager.VERTICAL,false)
                adapter = FollowAdapter(TakipcilerimList,this@TakipcilerPage)
                binding.takipcirc.adapter = adapter
                binding.takipcirc.layoutManager=layoutManager
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
        binding.takipcirc.apply {
            layoutManager = LinearLayoutManager(this@TakipcilerPage,
                LinearLayoutManager.VERTICAL,false)
            adapter = FollowAdapter(TakipcilerimList,this@TakipcilerPage)
            binding.takipcirc.adapter = adapter
            binding.takipcirc.layoutManager=layoutManager
        }
    }
}