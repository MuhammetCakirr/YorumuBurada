package com.muhammetcakir.benimurunum

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.muhammetcakir.benimurunum.Models.MyFuncs
import com.muhammetcakir.benimurunum.databinding.ActivitySettingsBinding
import id.ionbit.ionalert.IonAlert

class Settings : AppCompatActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("Users")
    var fonksiyonlar = MyFuncs()
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
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
        binding.hesaplayout.setOnClickListener {
            startActivity(Intent(this, EditProfile::class.java))
        }
        binding.cikislayout.setOnClickListener {
            IonAlert(this, IonAlert.WARNING_TYPE)
                .setTitleText("Çıkış Yapmak İstediğine Emin Misin?")
                .setCancelText("İptal")
                .setConfirmText("Çıkış Yap")
                .setConfirmClickListener(object : IonAlert.ClickListener {
                    override fun onClick(sDialog: IonAlert) {
                        auth.updateCurrentUser(auth.currentUser!!)
                        auth.signOut()
                        if (auth.currentUser == null) {
                            yenikikullanicilist.clear()
                            suankikullanicilist.clear()
                            val intent = Intent(applicationContext, SignIn::class.java)
                            startActivity(intent)
                        }

                        sDialog.dismissWithAnimation()
                    }
                })
                .setCancelClickListener(object : IonAlert.ClickListener {
                    override fun onClick(sDialog: IonAlert) {
                        sDialog.dismissWithAnimation()
                    }
                })
                .show()

        }

        binding.hesappsillayout.setOnClickListener {
            IonAlert(this, IonAlert.ERROR_TYPE)
                .setTitleText("Hesabını Silmek İstediğine Emin Misin?")
                .setContentText("Hesabını silersen yeni bir hesap oluşturmak zorunda kalacaksın.")
                .setCancelText("İptal")
                .setConfirmText("Hesabı Sil")
                .setConfirmClickListener(object : IonAlert.ClickListener {
                    override fun onClick(sDialog: IonAlert) {
                        sDialog.dismissWithAnimation()
                        removedatabase(auth.currentUser!!.uid.toString())
                        auth.currentUser!!.delete().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                            }
                        }
                        yenikikullanicilist.clear()
                        suankikullanicilist.clear()
                    }
                })
                .setCancelClickListener(object : IonAlert.ClickListener {
                    override fun onClick(sDialog: IonAlert) {
                        sDialog.dismissWithAnimation()
                    }
                })
                .show()

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
    private fun removedatabase(id: String) {
        database.child(id).removeValue()
            .addOnCompleteListener { task ->
                if (task.isComplete && task.isSuccessful) {
                    //back
                    Toast.makeText(
                        getApplicationContext(),
                        "Hesabınız Silindi. Çıkışa Yönlendirildiniz.",
                        Toast.LENGTH_LONG
                    )
                        .show();
                    startActivity(Intent(this, SignIn::class.java))
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}


