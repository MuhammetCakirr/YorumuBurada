package com.muhammetcakir.benimurunum

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.muhammetcakir.benimurunum.Models.LoadingDialog
import com.muhammetcakir.benimurunum.databinding.ActivityAddProductBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AddProduct : AppCompatActivity() {
    private  var db3: DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri? = null
    var indirimdurum: String = ""
    var onlinedurum: String = ""
    var platform:String=""
    var selectedBitmap: Bitmap? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        registerLauncher()
        binding.btnpostpaylas.setOnClickListener {
            val current=LocalDateTime.now()
            val formatter=DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("tr"))
            val formatted=current.format(formatter)
            val current2= LocalDateTime.now()
            val formatter2= DateTimeFormatter.ofPattern("HH:mm:ss", Locale("tr"))
            val formatted2=current2.format(formatter2)
            UploadStorage(binding.root,formatted,formatted2)
            val loading = LoadingDialog(this)
            loading.startLoading()
            val handler = Handler()
            handler.postDelayed(object :Runnable{
                override fun run() {
                    loading.isDismiss()
                }
            },5000)
            startActivity(Intent(this,MainPage::class.java))
        }
        binding.fotosec.setOnClickListener {
            imageViewClicked(binding.root)
        }
        binding.urunindirimdemi.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) indirimdurum = "1" else indirimdurum = "0"
        })
        binding.satinalinmasekli.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked)
            {
                onlinedurum = "1"
            }
            else {
                onlinedurum = "0"
            }
        }
        )
        if(binding.urunsatinalinanplatform.text.isEmpty())
        {
            platform = "Yok"
        }
        else
        {
            platform=binding.urunsatinalinanplatform.text.toString()
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
        binding.tarih.text = sdf.format(mycalender.time)
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
                                this@AddProduct.contentResolver,
                                selectedPicture!!
                            )
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.fotosec.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(
                                this@AddProduct.contentResolver,
                                selectedPicture
                            )
                            binding.fotosec.setImageBitmap(selectedBitmap)
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
                Toast.makeText(this@AddProduct, "Permisson needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun UploadStorage(view: View,tarih:String,siralamatarihi:String) {
        //UUID -> image name
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
                    val Postmap = hashMapOf<String, Any>()
                    Postmap.put("ImageUrl", downloadUrl.toString())
                    Postmap.put("id", uuid1.toString())
                    Postmap.put("aciklamasi", binding.urunaciklamasi.text.toString())
                    Postmap.put("urunadi", binding.Urunad.text.toString())
                    Postmap.put("urunfiyat", binding.urunfiyat.text.toString())
                    Postmap.put("magaza", binding.urunmagazasi.text.toString())
                    Postmap.put("kimeait", auth.currentUser!!.uid.toString())
                    Postmap.put("indirimdemi", indirimdurum.toString())
                    Postmap.put("onlinemi", onlinedurum.toString())
                    Postmap.put("alinmatarihi", binding.tarih.text.toString())
                    Postmap.put("puan", binding.puan.text.toString())
                    Postmap.put("platform", platform.toString())
                    Postmap.put("postpaylasilmatarihi",tarih.toString())
                    Postmap.put("siralamatarihi",siralamatarihi.toString())
                    db3.child(uuid1.toString()).setValue(Postmap)
                        .addOnCompleteListener { task ->
                            if (task.isComplete && task.isSuccessful) {
                                Toast.makeText(getApplicationContext(),"Paylaşıldı",Toast.LENGTH_LONG).show();
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
}


