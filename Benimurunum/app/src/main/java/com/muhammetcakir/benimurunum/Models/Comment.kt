package com.muhammetcakir.benimurunum.Models

data class Comment(
    var id: String? = null,
    var yorum:String?=null,
    var yorumyapankisiid:String?=null,
    var postsahibikisiid:String?=null,
    var postid:String?=null,
    var yorumatilmatarihi:String?=null,
) {
}