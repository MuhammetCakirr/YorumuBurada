package com.muhammetcakir.benimurunum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.muhammetcakir.benimurunum.Adapter.ProfilePostsAdapter
import com.muhammetcakir.benimurunum.ClickListeners.PostClick
import com.muhammetcakir.benimurunum.Models.MyFuncs
import com.muhammetcakir.benimurunum.Models.Post
import com.muhammetcakir.benimurunum.databinding.ActivitySavedPostsPageBinding

class SavedPostsPage : AppCompatActivity(), PostClick {
    private lateinit var binding: ActivitySavedPostsPageBinding
    var fonksiyonlar= MyFuncs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySavedPostsPageBinding.inflate(layoutInflater)
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
        binding.kaydedilenlerrc.apply {
            layoutManager= GridLayoutManager(context,3)
            adapter= ProfilePostsAdapter(KaydedilenPostlar,this@SavedPostsPage)
        }
        println("TÜM KAYDEDİLENLER"+ TumKaydedilenPostlar.count())
        println("BENİM KAYDEDİLENLER"+ KaydedilenPostlar.count())
    }

    override fun onClick(post: Post) {
        val intent = Intent(applicationContext, PostDetail::class.java)
        intent.putExtra(POST_ID_EXTRA, post.id.toString())
        startActivity(intent)
    }
}