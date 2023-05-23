package com.muhammetcakir.benimurunum

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.messaging.FirebaseMessaging
import com.muhammetcakir.benimurunum.Adapter.ChooseMessageAdapter
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.FirebaseService.Firebaseservice
import com.muhammetcakir.benimurunum.Models.MyFuncs
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.databinding.ActivityChooseMessagePageBinding
class ChooseMessagePage : AppCompatActivity(),UserSearchClick {
    private lateinit var binding:ActivityChooseMessagePageBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fonksiyonlar= MyFuncs()
    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChooseMessagePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        fonksiyonlar.Getkullanicilar()
        fonksiyonlar.GetPostlar()
        fonksiyonlar.GetTakipler()
        fonksiyonlar.GetBegeniler()
        fonksiyonlar.GetBildirimler()
        fonksiyonlar.GetKaydedilenPostlar()
        fonksiyonlar.GetMesajlar()
        fonksiyonlar.GetYorumlar()
        Firebaseservice.sharedPref=getSharedPreferences("sharedPref", MODE_PRIVATE)
            Firebaseservice.token = FirebaseInstanceIdReceiver().toString()
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/${auth.currentUser!!.uid.toString()}")
        for (mesaj in MesajlarList) {
            for (user in KullanicilarArrayList) {
                if (mesaj.mesajalanid == auth.currentUser!!.uid.toString() && mesaj.mesajgonderenid == user.id.toString()) {
                    if (MesajlastiklarimList.contains(user)) {

                    } else {
                        MesajlastiklarimList.add(user)
                    }
                    BenimMesajlarList.add(mesaj)
                } else if (mesaj.mesajgonderenid == auth.currentUser!!.uid.toString() && mesaj.mesajalanid == user.id.toString()) {
                    if (MesajlastiklarimList.contains(user)) {
                    } else {
                        MesajlastiklarimList.add(user)
                    }
                    BenimMesajlarList.add(mesaj)
                }
            }
        }
        binding.mesajlastiklarimrc.apply {
            layoutManager = LinearLayoutManager(this@ChooseMessagePage,
                LinearLayoutManager.VERTICAL,false)
            adapter = ChooseMessageAdapter(MesajlastiklarimList,this@ChooseMessagePage)
            binding.mesajlastiklarimrc.adapter = adapter
            binding.mesajlastiklarimrc.layoutManager=layoutManager
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
        binding.addmessagebutton.setOnClickListener {
            val intent = Intent(applicationContext, AddMessage::class.java)
            startActivity(intent)
        }
    }
    override fun Onclick(user: User) {
        val intent = Intent(applicationContext, Chat::class.java)
        intent.putExtra(USER_ID_EXTRA, user.id.toString())
        startActivity(intent)
    }
}


