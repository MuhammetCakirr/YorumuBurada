package com.muhammetcakir.benimurunum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muhammetcakir.benimurunum.Adapter.SearchUserFollowAdapter
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.Like
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.databinding.ActivityListofLikeBinding

class ListofLike : AppCompatActivity(), UserSearchClick {
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database6 = FirebaseDatabase.getInstance().getReference("Likes")
    private lateinit var binding: ActivityListofLikeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListofLikeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        val postId = intent.getStringExtra(POST_ID_EXTRA)
        println(" UserBegenenlerList" + UserBegenenlerList.count().toString())
        var getdata6 = database6.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                BegenilerList.clear()
                if (snapshot.exists()) {
                    for (notSnapshot in snapshot.children) {
                        var like = notSnapshot.getValue(Like::class.java)
                        BegenilerList.add(like!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )
        database6.addValueEventListener(getdata6)

        BegenilenPostlarList.clear()
        for (begeni in BegenilerList) {
            for (post in PostlarArrayList) {
                if (begeni.postid.toString() == post.id && begeni.begenenkisiid.toString() == auth.currentUser!!.uid.toString()) {
                    BegenilenPostlarList.add(post)
                }
            }
        }
        UserBegenenlerList.clear()
        for (begenme in BegenilerList) {
            for (user in KullanicilarArrayList) {
                if (user.id.toString() == begenme.begenenkisiid.toString() && postId.toString() == begenme.postid.toString()) {
                    UserBegenenlerList.add(user)
                }
            }
        }
        binding.begenmerc.apply {
            layoutManager = LinearLayoutManager(
                this@ListofLike,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = SearchUserFollowAdapter(UserBegenenlerList, this@ListofLike)
            binding.begenmerc.adapter = adapter
            binding.begenmerc.layoutManager = layoutManager
        }
    }

    override fun Onclick(user: User) {
        val intent = Intent(applicationContext, UserProfile::class.java)
        intent.putExtra(USER_ID_EXTRA, user.id.toString())
        startActivity(intent)
    }
}