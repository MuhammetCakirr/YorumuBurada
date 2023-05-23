package com.muhammetcakir.benimurunum

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.muhammetcakir.benimurunum.Models.Post
import com.muhammetcakir.benimurunum.databinding.ActivityEditPostBinding
import com.squareup.picasso.Picasso
import id.ionbit.ionalert.IonAlert
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class EditPost : AppCompatActivity() {
    private lateinit var binding: ActivityEditPostBinding
    private var db: DatabaseReference = Firebase.database.getReference("Posts")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        val postId = intent.getStringExtra(POST_ID_EXTRA)
        val post = postFromID(postId.toString())
        if (post != null) {
            Picasso.get().load(post.ImageUrl).into(binding.postfoto)
            binding.urunaciklama.setText(post.aciklamasi)
            binding.urunadi.setText(post.urunadi)
            binding.urunfiyati.setText(post.urunfiyat)
            binding.urunmagazasi.setText(post.magaza)
            binding.urunalNmatarihi.setText(post.alinmatarihi)
            binding.puan.setText("7.5")
            if (post.indirimdemi == "1") {
                binding.indirimdemi2.isChecked = true
            } else {
                binding.indirimdemi2.isChecked = false
            }
            if (post.onlinemi == "1") {
                binding.onlinemi2.isChecked = true
                binding.hangiplatform.setText("Trendyol")
            } else {
                binding.onlinemi2.isChecked = false
                binding.hangiplatform.setText("-")
            }
            binding.btnkaydet.setOnClickListener {
                IonAlert(this, IonAlert.WARNING_TYPE)
                    .setTitleText("Değişiklik Yapmak İstediğine Emin Misin?")
                    .setCancelText("İptal")
                    .setConfirmText("Kaydet")
                    .setConfirmClickListener(object : IonAlert.ClickListener {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onClick(sDialog: IonAlert) {
                            val current = LocalDateTime.now()
                            val formatter =
                                DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("tr"))
                            val formatted = current.format(formatter)
                            val current2 = LocalDateTime.now()
                            val formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss", Locale("tr"))
                            val formatted2 = current2.format(formatter2)
                            Upgrade(post, formatted, formatted2)
                            startActivity(Intent(this@EditPost, Profile::class.java))
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
        }
        val mycalender = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            mycalender.set(Calendar.YEAR, year)
            mycalender.set(Calendar.MONTH, month)
            mycalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(mycalender)

        }
        binding.tarihsec.setOnClickListener {
            DatePickerDialog(
                this, datePicker, mycalender.get(Calendar.YEAR), mycalender.get(Calendar.MONTH),
                mycalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateLable(mycalender: Calendar) {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("tr"))
        binding.urunalNmatarihi.setText(sdf.format(mycalender.time))
    }

    private fun Upgrade(post: Post, tarih: String, siralamatarihi: String) {
        val FollowMap = hashMapOf<String, Any>()
        FollowMap.put("id", post.id.toString())
        FollowMap.put("kimeait", post.kimeait.toString())
        FollowMap.put("magaza", binding.urunmagazasi.text.toString())
        FollowMap.put("urunfiyat", binding.urunfiyati.text.toString())
        FollowMap.put("urunadi", binding.urunadi.text.toString())
        FollowMap.put("alinmatarihi", binding.urunalNmatarihi.text.toString())
        FollowMap.put("aciklamasi", binding.urunaciklama.text.toString())
        FollowMap.put("onlinemi", "1")
        FollowMap.put("indirimdemi", "1")
        FollowMap.put("ImageUrl", post.ImageUrl.toString())
        FollowMap.put("puan", binding.puan.text.toString())
        FollowMap.put("platform", binding.hangiplatform.text.toString())
        FollowMap.put("postpaylasilmatarihi", tarih.toString())
        FollowMap.put("siralamatarihi", siralamatarihi.toString())
        db.child(post.id.toString()).setValue(FollowMap)
            .addOnCompleteListener { task ->
                if (task.isComplete && task.isSuccessful) {
                    //back
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun postFromID(postId: String): Post? {
        for (post in PostlarArrayList) {
            if (post.id == postId)
                return post
        }
        return null
    }
}
