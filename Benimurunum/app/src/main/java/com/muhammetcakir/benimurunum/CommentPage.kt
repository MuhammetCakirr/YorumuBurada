package com.muhammetcakir.benimurunum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muhammetcakir.benimurunum.Adapter.CommentAdapter
import com.muhammetcakir.benimurunum.Adapter.SearchUserFollowAdapter
import com.muhammetcakir.benimurunum.ClickListeners.UserSearchClick
import com.muhammetcakir.benimurunum.Models.Comment
import com.muhammetcakir.benimurunum.Models.Post
import com.muhammetcakir.benimurunum.Models.User
import com.muhammetcakir.benimurunum.databinding.ActivityCommentPageBinding
import com.muhammetcakir.benimurunum.databinding.ActivityNotificationPageBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CommentPage : AppCompatActivity(), UserSearchClick {
    private lateinit var binding: ActivityCommentPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val database2 = FirebaseDatabase.getInstance().getReference("Comments")
        super.onCreate(savedInstanceState)
        binding = ActivityCommentPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val postId = intent.getStringExtra(POST_ID_EXTRA)

        var getdata2 = database2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                YorumlarList.clear()
                if (snapshot.exists()) {
                    for (postSnapshot in snapshot.children) {
                        var comment = postSnapshot.getValue(Comment::class.java)
                        YorumlarList.add(comment!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )
        database2.addListenerForSingleValueEvent(getdata2)
        PostunYorumlarList.clear()
        for (yorum in YorumlarList) {
            if (yorum.postid.toString() == postId.toString()) {
                PostunYorumlarList.add(yorum)
            }
        }
        binding.commentrc.apply {
            layoutManager = LinearLayoutManager(
                this@CommentPage,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = CommentAdapter(PostunYorumlarList, this@CommentPage)
            binding.commentrc.adapter = adapter
            binding.commentrc.layoutManager = layoutManager
        }

    }

    override fun Onclick(user: User) {
        val intent = Intent(applicationContext, UserProfile::class.java)
        intent.putExtra(USER_ID_EXTRA, user.id.toString())
        startActivity(intent)
    }
}