package com.muhammetcakir.benimurunum.Models

import com.google.firebase.database.*
import com.muhammetcakir.benimurunum.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MyFuncs
{
    val database = FirebaseDatabase.getInstance().getReference("Users")
    val database2 = FirebaseDatabase.getInstance().getReference("SavePosts")
    val database3 = FirebaseDatabase.getInstance().getReference("Comments")
    val database4 = FirebaseDatabase.getInstance().getReference("Posts")
    val database5 = FirebaseDatabase.getInstance().getReference("Notification")
    val database7 = FirebaseDatabase.getInstance().getReference("Follows")
    val database6 = FirebaseDatabase.getInstance().getReference("Likes")
    val database8= FirebaseDatabase.getInstance().getReference("Messages")

    fun Getkullanicilar()
    {
        database.addValueEventListener(object : ChildEventListener, ValueEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    KullanicilarArrayList.clear()
                    for (userSnapshot in snapshot.children) {
                        var user = userSnapshot.getValue(User::class.java)
                        KullanicilarArrayList.add(user!!)
                    }
                }
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                if (snapshot.exists()) {
                    KullanicilarArrayList.clear()
                    for (userSnapshot in snapshot.children) {
                        var user = userSnapshot.getValue(User::class.java)
                        KullanicilarArrayList.add(user!!)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    KullanicilarArrayList.clear()
                    for (userSnapshot in snapshot.children) {
                        var user = userSnapshot.getValue(User::class.java)
                        KullanicilarArrayList.add(user!!)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    KullanicilarArrayList.clear()
                    for (userSnapshot in snapshot.children) {
                        var user = userSnapshot.getValue(User::class.java)
                        KullanicilarArrayList.add(user!!)
                    }
                }
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    KullanicilarArrayList.clear()
                    for (userSnapshot in snapshot.children) {
                        var user = userSnapshot.getValue(User::class.java)
                        KullanicilarArrayList.add(user!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun GetKaydedilenPostlar()
    {
        database2.addValueEventListener(object : ChildEventListener, ValueEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    TumKaydedilenPostlar.clear()
                    for (postSnapshot in snapshot.children) {
                        var post = postSnapshot.getValue(Postofsaved::class.java)
                        TumKaydedilenPostlar.add(post!!)
                    }
                }
            }
            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                if (snapshot.exists()) {
                    TumKaydedilenPostlar.clear()
                    for (postSnapshot in snapshot.children) {
                        var post = postSnapshot.getValue(Postofsaved::class.java)
                        TumKaydedilenPostlar.add(post!!)
                    }
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    TumKaydedilenPostlar.clear()
                    for (postSnapshot in snapshot.children) {
                        var post = postSnapshot.getValue(Postofsaved::class.java)
                        TumKaydedilenPostlar.add(post!!)
                    }
                }
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    TumKaydedilenPostlar.clear()
                    for (postSnapshot in snapshot.children) {
                        var post = postSnapshot.getValue(Postofsaved::class.java)
                        TumKaydedilenPostlar.add(post!!)
                    }
                }
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    TumKaydedilenPostlar.clear()
                    for (postSnapshot in snapshot.children) {
                        var post = postSnapshot.getValue(Postofsaved::class.java)
                        TumKaydedilenPostlar.add(post!!)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun GetYorumlar()
    {
        database3.addValueEventListener(object : ChildEventListener, ValueEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    YorumlarList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Comment::class.java)
                        YorumlarList.add(notification!!)
                    }
                }
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                if (snapshot.exists()) {
                    YorumlarList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Comment::class.java)
                        YorumlarList.add(notification!!)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    YorumlarList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Comment::class.java)
                        YorumlarList.add(notification!!)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    YorumlarList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Comment::class.java)
                        YorumlarList.add(notification!!)
                    }
                }
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    YorumlarList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Comment::class.java)
                        YorumlarList.add(notification!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
     fun GetPostlar()
    {
        database4.addValueEventListener(object : ChildEventListener, ValueEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    PostlarArrayList.clear()
                    for (notSnapshot in snapshot.children) {
                        var post = notSnapshot.getValue(Post::class.java)
                        PostlarArrayList.add(post!!)
                    }
                }
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                if (snapshot.exists()) {
                    PostlarArrayList.clear()
                    for (notSnapshot in snapshot.children) {
                        var post = notSnapshot.getValue(Post::class.java)
                        PostlarArrayList.add(post!!)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    PostlarArrayList.clear()
                    for (notSnapshot in snapshot.children) {
                        var post = notSnapshot.getValue(Post::class.java)
                        PostlarArrayList.remove(post!!)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    PostlarArrayList.clear()
                    for (notSnapshot in snapshot.children) {
                        var post = notSnapshot.getValue(Post::class.java)
                        PostlarArrayList.add(post!!)
                    }
                }
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    PostlarArrayList.clear()
                    for (notSnapshot in snapshot.children) {
                        var post = notSnapshot.getValue(Post::class.java)
                        PostlarArrayList.add(post!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun GetBildirimler()
    {
        database5.addValueEventListener(object : ChildEventListener, ValueEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    BildirimlerList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Notification::class.java)
                        BildirimlerList.add(notification!!)
                    }
                }
            }
            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                if (snapshot.exists()) {
                    BildirimlerList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Notification::class.java)
                        BildirimlerList.add(notification!!)
                    }
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    BildirimlerList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Notification::class.java)
                        BildirimlerList.add(notification!!)
                    }
                }
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    BildirimlerList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Notification::class.java)
                        BildirimlerList.add(notification!!)
                    }
                }
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    BildirimlerList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Notification::class.java)
                        BildirimlerList.add(notification!!)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun GetBegeniler()
    {
        database6.addValueEventListener(object : ChildEventListener, ValueEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    BegenilerList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Like::class.java)
                        BegenilerList.add(notification!!)
                    }
                }
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                if (snapshot.exists()) {
                    BegenilerList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Like::class.java)
                        BegenilerList.add(notification!!)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    BegenilerList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Like::class.java)
                        BegenilerList.add(notification!!)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    BegenilerList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Like::class.java)
                        BegenilerList.add(notification!!)
                    }
                }
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    BegenilerList.clear()
                    for (notSnapshot in snapshot.children) {
                        var notification = notSnapshot.getValue(Like::class.java)
                        BegenilerList.add(notification!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun GetTakipler()
    {
        database7.addValueEventListener(object : ChildEventListener, ValueEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    followArrayList.clear()
                    for (notSnapshot in snapshot.children) {
                        var follow = notSnapshot.getValue(Follow::class.java)
                        followArrayList.add(follow!!)
                    }
                }
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                if (snapshot.exists()) {
                    followArrayList.clear()
                    for (notSnapshot in snapshot.children) {
                        var follow = notSnapshot.getValue(Follow::class.java)
                        followArrayList.add(follow!!)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    followArrayList.clear()
                    for (notSnapshot in snapshot.children) {
                        var follow = notSnapshot.getValue(Follow::class.java)
                        followArrayList.add(follow!!)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    followArrayList.clear()
                    for (notSnapshot in snapshot.children) {
                        var follow = notSnapshot.getValue(Follow::class.java)
                        followArrayList.add(follow!!)
                    }
                }
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    followArrayList.clear()
                    for (notSnapshot in snapshot.children) {
                        var follow = notSnapshot.getValue(Follow::class.java)
                        followArrayList.add(follow!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun GetMesajlar()
    {
        database8.addValueEventListener(object : ChildEventListener, ValueEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    MesajlarList.clear()
                    for (notSnapshot in snapshot.children) {
                        var mesaj = notSnapshot.getValue(Message::class.java)
                        MesajlarList.add(mesaj!!)
                    }
                }
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                if (snapshot.exists()) {
                    MesajlarList.clear()
                    for (notSnapshot in snapshot.children) {
                        var mesaj = notSnapshot.getValue(Message::class.java)
                        MesajlarList.add(mesaj!!)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    MesajlarList.clear()
                    for (notSnapshot in snapshot.children) {
                        var mesaj = notSnapshot.getValue(Message::class.java)
                        MesajlarList.add(mesaj!!)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    MesajlarList.clear()
                    for (notSnapshot in snapshot.children) {
                        var mesaj = notSnapshot.getValue(Message::class.java)
                        MesajlarList.add(mesaj!!)
                    }
                }
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    MesajlarList.clear()
                    for (notSnapshot in snapshot.children) {
                        var mesaj = notSnapshot.getValue(Message::class.java)
                        MesajlarList.add(mesaj!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}