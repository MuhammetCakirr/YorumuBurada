package com.muhammetcakir.benimurunum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammetcakir.benimurunum.Adapter.ChooseMessageAdapter
import com.muhammetcakir.benimurunum.Adapter.FollowAdapter
import com.muhammetcakir.benimurunum.Adapter.SearchUserFollowAdapter
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.MyFuncs
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.databinding.ActivityAddMessageBinding

class AddMessage  : AppCompatActivity(), UserSearchClick {
    private lateinit var binding: ActivityAddMessageBinding
    var fonksiyonlar = MyFuncs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMessageBinding.inflate(layoutInflater)
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
        binding.yenimesajrc.apply {
            layoutManager = LinearLayoutManager(
                this@AddMessage,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = SearchUserFollowAdapter(TakipEttiklerimList, this@AddMessage)
            binding.yenimesajrc.adapter = adapter
            binding.yenimesajrc.layoutManager = layoutManager
        }

        binding.backbutton.setOnClickListener {
            val intent = Intent(this, ChooseMessagePage::class.java)
            startActivity(intent)
        }
    }

    override fun Onclick(user: User) {
        val intent = Intent(applicationContext, Chat::class.java)
        intent.putExtra(USER_ID_EXTRA, user.id.toString())
        startActivity(intent)
    }
}