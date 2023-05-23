package com.muhammetcakir.benimurunum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammetcakir.benimurunum.Adapter.SearchUserFollowAdapter
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.MyFuncs
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.databinding.ActivityUserTakipcilerBinding

class UserTakipciler : AppCompatActivity(),UserSearchClick {
    private lateinit var binding:ActivityUserTakipcilerBinding
    var fonksiyonlar= MyFuncs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserTakipcilerBinding.inflate(layoutInflater)
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
            binding.takipcirc.apply {
                layoutManager = LinearLayoutManager(this@UserTakipciler,
                    LinearLayoutManager.VERTICAL,false)
                adapter = SearchUserFollowAdapter(SearchTakipcileriList,this@UserTakipciler)
                binding.takipcirc.adapter = adapter
                binding.takipcirc.layoutManager=layoutManager
            }
        binding.backbutton.setOnClickListener {

        }
    }

    override fun Onclick(user: User) {
        val intent = Intent(applicationContext, UserProfile::class.java)
        intent.putExtra(USER_ID_EXTRA, user.id.toString())
        startActivity(intent)
    }
}