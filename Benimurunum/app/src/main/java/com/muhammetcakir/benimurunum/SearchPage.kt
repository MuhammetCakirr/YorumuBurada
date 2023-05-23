package com.muhammetcakir.benimurunum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammetcakir.benimurunum.Adapter.SearchAdapter
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.MyFuncs
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.databinding.ActivityMainPageBinding
import com.muhammetcakir.benimurunum.databinding.ActivitySearchPageBinding

class SearchPage : AppCompatActivity(), UserSearchClick {
    private lateinit var binding: ActivitySearchPageBinding
    var fonksiyonlar = MyFuncs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchPageBinding.inflate(layoutInflater)
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
        arananaKullanici.clear()
        binding.search.clearFocus()
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!=null)
                {
                    arananaKullanici.clear()
                for (User in KullanicilarArrayList) {
                    if (newText != null) {
                        if (User.kullaniciadi!!.toLowerCase().contains(newText.toLowerCase())) {
                            arananaKullanici.add(User)
                        }
                    }
                }
                binding.searchrc.apply {
                    layoutManager = LinearLayoutManager(
                        this@SearchPage,
                        LinearLayoutManager.VERTICAL, false
                    )
                    adapter = SearchAdapter(arananaKullanici, this@SearchPage)
                    binding.searchrc.adapter = adapter
                    binding.searchrc.layoutManager = layoutManager
                }
                }
               /* else{
                    arananaKullanici.clear()
                    binding.searchrc.apply {
                        layoutManager = LinearLayoutManager(
                            this@SearchPage,
                            LinearLayoutManager.VERTICAL, false
                        )
                        adapter = SearchAdapter(arananaKullanici, this@SearchPage)
                        binding.searchrc.adapter = adapter
                        binding.searchrc.layoutManager = layoutManager
                    }
                }*/
                return true
            }
        })
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

    override fun Onclick(user: User) {
        val intent = Intent(applicationContext, UserProfile::class.java)
        intent.putExtra(USER_ID_EXTRA, user.id.toString())
        startActivity(intent)
    }
}