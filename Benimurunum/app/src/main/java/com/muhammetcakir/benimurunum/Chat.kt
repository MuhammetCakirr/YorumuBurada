package com.muhammetcakir.benimurunum

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.muhammetcakir.benimurunum.Adapter.MessageAdapter
import com.muhammetcakir.benimurunum.Models.*
import com.muhammetcakir.benimurunum.databinding.ActivityChatBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
val benveoMesajlarList: ArrayList<Message> = ArrayList()
class Chat : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("Messages").orderByChild("mesajatilmatarihi")
    val database2 = FirebaseDatabase.getInstance().getReference("Messages")
    var topic = ""
    var fonksiyonlar=MyFuncs()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatBinding.inflate(layoutInflater)
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
        val userId = intent.getStringExtra(USER_ID_EXTRA)
        val user = userFromID(userId.toString())
        benveoMesajlarList.clear()
        if(user!=null)
        {
            for (mesajlar in BenimMesajlarList)
            {
                if (mesajlar.mesajalanid.toString()==auth.currentUser!!.uid.toString() && mesajlar.mesajgonderenid==user.id.toString()
                    ||mesajlar.mesajalanid.toString()==user.id.toString() && mesajlar.mesajgonderenid==auth.currentUser!!.uid.toString() )
                {
                    if(benveoMesajlarList.contains(mesajlar))
                    {

                    }
                    else{
                        benveoMesajlarList.add(mesajlar)
                    }
                }
            }
            binding.mesajlarrc.apply {
                layoutManager = LinearLayoutManager(this@Chat,
                    LinearLayoutManager.VERTICAL,false)
                adapter = MessageAdapter(benveoMesajlarList,user)
                binding.mesajlarrc.adapter = adapter
                binding.mesajlarrc.layoutManager=layoutManager
            }
            binding.kullaniciadi.text=user.kullaniciadi.toString()
            binding.sonmesaj.text=user.isim.toString()
            Picasso.get().load(user.ImageUrl).into(binding.kullanicifoto)
            binding.mesajgonderbtn.setOnClickListener {
                val current= LocalDateTime.now()
                val formatter= DateTimeFormatter.ofPattern("HH:mm:ss", Locale("tr"))
                val formatted=current.format(formatter)
                val current2= LocalDateTime.now()
                val formatter2= DateTimeFormatter.ofPattern("dd MMMM ", Locale("tr"))
                val formatted2=current2.format(formatter2)
                var mesaj:String
                mesaj=binding.mesaj.text.toString()
                var kullaniciadi2:String=""
                for (user in KullanicilarArrayList)
                {
                    if(user.id.toString()==auth.currentUser!!.uid.toString())
                    {
                        kullaniciadi2=user.kullaniciadi.toString()
                    }
                }
                SendMessage(user.id.toString(),formatted,formatted2,kullaniciadi2!!.toString(),mesaj)
                binding.mesaj.text.clear()
            }
            readMessage(auth.currentUser!!.uid.toString(),user!!.id.toString(),user)
        }
    }
    private fun SendMessage(mesajialan: String,tarih:String,sonmesajtarih:String,mesaıalankkullaniciadi:String,mesaj:String) {
        val uuid1 = UUID.randomUUID()
        val Postmap = hashMapOf<String, Any>()
        Postmap.put("id", uuid1.toString())
        Postmap.put("mesajgonderenid", auth.currentUser!!.uid.toString())
        Postmap.put("mesajalanid", mesajialan.toString())
        Postmap.put("mesaj", binding.mesaj.text.toString())
        Postmap.put("mesajatilmatarihi", tarih.toString())
        Postmap.put("sonmesajatilmatarihi", sonmesajtarih.toString())
        database2.child(uuid1.toString()).setValue(Postmap)
            .addOnCompleteListener { task ->
                if (task.isComplete && task.isSuccessful) {
                    Toast.makeText(getApplicationContext(), "Mesaj Gönderildi.", Toast.LENGTH_LONG).show();
                    topic = "/topics/$mesajialan"
                    PushNotification(NotificationData( mesaıalankkullaniciadi!!,mesaj),
                        topic).also {
                        sendNotification(it)
                    }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
    private fun userFromID(userId: String): User? {
        for (user in TakipEttiklerimList) {
            if (user.id.toString() == userId)
                return user
        }
        return null
    }
    private fun readMessage(senderId: String, receiverId: String,user:User) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                benveoMesajlarList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Message::class.java)

                    if (chat!!.mesajgonderenid.equals(senderId)&&chat!!.mesajalanid.equals(receiverId) ||
                        chat!!.mesajgonderenid.equals(receiverId) && chat!!.mesajalanid.equals(senderId)
                    ) {
                        benveoMesajlarList.add(chat)
                    }
                }
                val chatAdapter = MessageAdapter(benveoMesajlarList,user)
                binding.mesajlarrc.adapter = chatAdapter
            }
        })
    }
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch(e: Exception) {
            Log.e("TAG", e.toString())
        }
    }
}