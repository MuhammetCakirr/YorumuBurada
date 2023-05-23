package com.muhammetcakir.benimurunum

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muhammetcakir.benimurunum.Models.*
import com.muhammetcakir.benimurunum.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val POST_ID_EXTRA = "postextra"
val USER_ID_EXTRA = "userextra"
val PAGE_ID_EXTRA = "pageextra"

val yenikikullanicilist: ArrayList<User> = ArrayList()
val followArrayList: ArrayList<Follow> = ArrayList()
val TakipEttiklerimList: ArrayList<User> = ArrayList()
val TakipcilerimList: ArrayList<User> = ArrayList()
val UserBegenenlerList: ArrayList<User> = ArrayList()
val SearchTakipEttikleriList: ArrayList<User> = ArrayList()
val SearchTakipcileriList: ArrayList<User> = ArrayList()
val Postsahibi: ArrayList<User> = ArrayList()
val suankikullanicilist: ArrayList<User> = ArrayList()
val KullanicilarArrayList: ArrayList<User> = ArrayList()
val PostlarArrayList: ArrayList<Post> = ArrayList()
val SeciliPostArrayList: ArrayList<Post> = ArrayList()
val SearchUserPostArrayList: ArrayList<Post> = ArrayList()
val arananaKullanici: ArrayList<User> = ArrayList()
val TumKaydedilenPostlar: ArrayList<Postofsaved> = ArrayList()
val KaydedilenPostlar: ArrayList<Post> = ArrayList()
val BildirimlerList: ArrayList<Notification> = ArrayList()
val BenimBildirimlerList: ArrayList<Notification> = ArrayList()
val BegenilerList: ArrayList<Like> = ArrayList()
val BegenilenPostlarList: ArrayList<Post> = ArrayList()
val YorumlarList: ArrayList<Comment> = ArrayList()
val PostunYorumlarList: ArrayList<Comment> = ArrayList()
val AnasayfaPostlar: ArrayList<Post> = ArrayList()
val MesajlarList: ArrayList<Message> = ArrayList()
val BenimMesajlarList: ArrayList<Message> = ArrayList()
val MesajlastiklarimList: ArrayList<User> = ArrayList()

class MainActivity : AppCompatActivity() {
    var fonksiyonlar=MyFuncs()
    private var auth2: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        Handler().postDelayed(
            {
                fonksiyonlar.Getkullanicilar()
                fonksiyonlar.GetPostlar()
                fonksiyonlar.GetTakipler()
                fonksiyonlar.GetBegeniler()
                fonksiyonlar.GetBildirimler()
                fonksiyonlar.GetKaydedilenPostlar()
                fonksiyonlar.GetMesajlar()
                fonksiyonlar.GetYorumlar()
                val loading = LoadingDialog(this)
                loading.startLoading()
                val handler = Handler()
                handler.postDelayed(object :Runnable{
                    override fun run() {
                        loading.isDismiss()
                        AnasayfaPostlar.clear()
                        for (post in PostlarArrayList)
                        {
                            for (takipettiklerim in TakipcilerimList)
                            {
                                if (takipettiklerim.id.toString()==post.kimeait.toString())
                                {
                                    AnasayfaPostlar.add(post)
                                }
                            }
                        }
                        if (auth2.currentUser!=null){
                            suankikullanicilist.clear()
                            for (user in KullanicilarArrayList)
                            {
                                if (user.email.toString()==auth2.currentUser!!.email.toString())
                                {
                                    suankikullanicilist.add(user)
                                }
                            }
                            TakipEttiklerimList.clear()
                            for (follow in followArrayList) {
                                if (follow.takipedenkisiid.toString() == auth2.currentUser!!.uid.toString()) {

                                    for (user in KullanicilarArrayList) {
                                        if (follow.takipedilenkisiid == user.id.toString()) {
                                            TakipEttiklerimList.add(user)
                                        }
                                    }
                                }
                                println("İF ÇALIŞMADI")
                            }
                            startActivity(Intent(this@MainActivity, MainPage::class.java))
                        }
                        else{
                            startActivity(Intent(this@MainActivity, SignIn::class.java))
                        }
                    }
                },1000)
               //startActivity(Intent(this, SignIn::class.java))
            },1000
        )
    }
}