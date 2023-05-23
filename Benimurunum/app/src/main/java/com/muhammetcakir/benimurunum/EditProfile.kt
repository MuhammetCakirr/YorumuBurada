package com.muhammetcakir.benimurunum

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.muhammetcakir.benimurunum.Models.MyFuncs
import com.muhammetcakir.benimurunum.databinding.ActivityChatBinding
import com.muhammetcakir.benimurunum.databinding.ActivityEditProfileBinding
import com.squareup.picasso.Picasso
import id.ionbit.ionalert.IonAlert
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.util.*

class EditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: DatabaseReference = Firebase.database.reference
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri? = null
    var selectedBitmap: Bitmap? = null
    var fonksiyonlar = MyFuncs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
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
        registerLauncher()
        if (suankikullanicilist.isEmpty())
        {
            binding.email.setText(yenikikullanicilist[0].email.toString())
            binding.sifre.setText(yenikikullanicilist[0].sifre.toString())
            binding.kullaniciadi.setText(yenikikullanicilist[0].kullaniciadi.toString())
            binding.isimsoyisim.setText(yenikikullanicilist[0].isim.toString())
            Picasso.get().load(yenikikullanicilist[0].ImageUrl).into(binding.userphoto)
        }
        else{
            binding.email.setText(suankikullanicilist[0].email.toString())
            binding.sifre.setText(suankikullanicilist[0].sifre.toString())
            binding.kullaniciadi.setText(suankikullanicilist[0].kullaniciadi.toString())
            binding.isimsoyisim.setText(suankikullanicilist[0].isim.toString())
            Picasso.get().load(suankikullanicilist[0].ImageUrl).into(binding.userphoto)
        }
        binding.userphoto.setOnClickListener {
            imageViewClicked(binding.root)
        }
        binding.kaydetbtn.setOnClickListener {
            IonAlert(this, IonAlert.WARNING_TYPE)
                .setTitleText("Değişiklik Yapmak İstediğine Emin Misin?")
                .setCancelText("İptal")
                .setConfirmText("Güncelle")
                .setConfirmClickListener(object : IonAlert.ClickListener {
                    override fun onClick(sDialog: IonAlert) {
                        sDialog.dismissWithAnimation()
                        runBlocking {
                            launch {
                                delay(3000)
                                Upgrade()
                            }
                        }
                        startActivity(Intent(applicationContext, Profile::class.java))

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
    private fun Upgrade() {
        val uuid1 = UUID.randomUUID()
        val imageName = "$uuid1.jpg"
        val storage = Firebase.storage
        val reference = storage.reference
        val imagesReference = reference.child("images").child(imageName)
        if (selectedPicture != null) {
            imagesReference.putFile(selectedPicture!!).addOnSuccessListener { taskSnapshot ->
                val uploadedPictureReference = storage.reference.child("images").child(imageName)
                uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    val FollowMap = hashMapOf<String, Any>()
                    FollowMap.put("id", auth.currentUser!!.uid.toString())
                    FollowMap.put("email", binding.email.text.toString())
                    FollowMap.put("sifre", binding.sifre.text.toString())
                    FollowMap.put("isim", binding.isimsoyisim.text.toString())
                    FollowMap.put("kullaniciadi", binding.kullaniciadi.text.toString())
                    FollowMap.put("ImageUrl", downloadUrl)
                    suankikullanicilist[0].isim = binding.isimsoyisim.text.toString()
                    suankikullanicilist[0].sifre = binding.sifre.text.toString()
                    suankikullanicilist[0].kullaniciadi = binding.kullaniciadi.text.toString()
                    suankikullanicilist[0].ImageUrl = downloadUrl.toString()
                    db.child("Users").child(auth.currentUser!!.uid.toString()).setValue(FollowMap)
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
            }
        } else {
            val FollowMap = hashMapOf<String, Any>()
            FollowMap.put("id", auth.currentUser!!.uid.toString())
            FollowMap.put("email", binding.email.text.toString())
            FollowMap.put("sifre", binding.sifre.text.toString())
            FollowMap.put("isim", binding.isimsoyisim.text.toString())
            FollowMap.put("kullaniciadi", binding.kullaniciadi.text.toString())
            FollowMap.put("ImageUrl", suankikullanicilist[0].ImageUrl.toString())
            suankikullanicilist[0].isim = binding.isimsoyisim.text.toString()
            suankikullanicilist[0].sifre = binding.sifre.text.toString()
            suankikullanicilist[0].kullaniciadi = binding.kullaniciadi.text.toString()
            db.child("Users").child(auth.currentUser!!.uid.toString()).setValue(FollowMap)
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
    }
    fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    selectedPicture = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(
                                this@EditProfile.contentResolver,
                                selectedPicture!!
                            )
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.userphoto.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(
                                this@EditProfile.contentResolver,
                                selectedPicture
                            )
                            binding.userphoto.setImageBitmap(selectedBitmap)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result ->
            if (result) {
                //permission granted
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //permission denied
                Toast.makeText(this@EditProfile, "Permisson needed!", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun imageViewClicked(view: View) {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Snackbar.make(view, "Galeriye girme izni gerekli", Snackbar.LENGTH_INDEFINITE)
                    .setAction("İzin ver",
                        View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)

        }
    }
}