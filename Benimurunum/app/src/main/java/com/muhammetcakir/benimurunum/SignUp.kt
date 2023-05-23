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
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.databinding.ActivitySignUpBinding
import java.io.IOException
import java.util.*

private var auth: FirebaseAuth = FirebaseAuth.getInstance()

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var db: DatabaseReference = Firebase.database.reference
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri? = null
    var selectedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        registerLauncher()

        binding.kayitolbutton.setOnClickListener {
            signUpClicked(binding.root)
        }
        binding.zatenhesabinvarmi.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
        }
        binding.fotosecbtn.setOnClickListener {
            imageViewClicked(binding.root)
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
                                this@SignUp.contentResolver,
                                selectedPicture!!
                            )
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.foto.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(
                                this@SignUp.contentResolver,
                                selectedPicture
                            )
                            binding.foto.setImageBitmap(selectedBitmap)
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
                Toast.makeText(this@SignUp, "Permisson needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun DatabaseUpload(view: View) {
        val userEmail = binding.signupemail.text.toString()
        val password = binding.signupadsifre.text.toString()
        val isim = binding.signupadsoyad.text.toString()
        val kullaniciadi = binding.signupkullaniciadi.text.toString()
        val uuid1 = UUID.randomUUID()
        val imageName = "$uuid1.jpg"
        val storage = Firebase.storage
        val reference = storage.reference
        val imagesReference = reference.child("images").child(imageName)
        if (binding.signupemail.text.isNotEmpty() && binding.signupadsifre.text.isNotEmpty() && binding.signupkullaniciadi.text.isNotEmpty() && binding.signupadsoyad.text.isNotEmpty()) {
            imagesReference.putFile(selectedPicture!!).addOnSuccessListener { taskSnapshot ->
                val uploadedPictureReference = storage.reference.child("images").child(imageName)
                uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    val userMap = hashMapOf<String, Any>()
                    userMap.put("id", auth.currentUser!!.uid)
                    userMap.put("ImageUrl", downloadUrl)
                    userMap.put("email", userEmail)
                    userMap.put("isim", isim)
                    userMap.put("sifre", password)
                    userMap.put("kullaniciadi", kullaniciadi)
                    db.child("Users").child(auth.currentUser!!.uid).setValue(userMap)
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
        }
    }

    fun signUpClicked(view: View) {
        val userEmail = binding.signupemail.text.toString()
        val password = binding.signupadsifre.text.toString()
        val isim = binding.signupadsoyad.text.toString()
        val kullaniciadi = binding.signupkullaniciadi.text.toString()
        if (userEmail.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    DatabaseUpload(view)
                    yenikikullanicilist.clear()
                    yenikikullanicilist.add(
                        User(
                            auth.currentUser!!.uid,
                            userEmail,
                            password,
                            binding.signupadsoyad.text.toString(),
                            kullaniciadi,
                            selectedPicture.toString(),
                        )
                    )
                    Toast.makeText(
                        applicationContext,
                        "Aramıza Hoşgeldin: ${auth.currentUser?.email.toString()}",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this, MainPage::class.java))
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}