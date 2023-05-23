package com.muhammetcakir.benimurunum.ClickListeners

import com.muhammetcakir.benimurunum.Models.Comment
import com.muhammetcakir.benimurunum.Models.Post

interface CommentClick {
    fun onclick(post: Post)
}