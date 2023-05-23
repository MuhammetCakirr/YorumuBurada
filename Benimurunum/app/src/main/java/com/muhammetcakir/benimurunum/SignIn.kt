package com.muhammetcakir.benimurunum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.muhammetcakir.benimurunum.Models.LoadingDialog
import com.muhammetcakir.benimurunum.databinding.ActivitySignInBinding



class SignIn : AppCompatActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.girisyapbutton.setOnClickListener {
            signInClicked(binding.root)
            val loading = LoadingDialog(this)
            loading.startLoading()
            val handler = Handler()
            handler.postDelayed(object :Runnable{
                override fun run() {
                    loading.isDismiss()

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
                    if (auth.currentUser!=null){
                        suankikullanicilist.clear()
                        for (user in KullanicilarArrayList)
                        {
                            if (user.email.toString()==auth.currentUser!!.email.toString())
                            {
                                suankikullanicilist.add(user)
                            }
                        }
                        startActivity(Intent(this@SignIn, MainPage::class.java))
                    }
                    else{
                    }
                }

            },3000)

        }
        binding.ucretsizkayitoltext.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }
    fun signInClicked(view: View) {
        val userEmail = binding.SigninEmail.text.toString()
        val password = binding.SigninSifre.text.toString()
        if (userEmail.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(userEmail, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Hoşgeldin: ${auth.currentUser?.email.toString()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}