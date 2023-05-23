package com.muhammetcakir.benimurunum.Models

data class Message(
    var id: String? = null,
    var mesajgonderenid:String?=null,
    var mesajalanid:String?=null,
    var mesaj:String?=null,
    var mesajatilmatarihi:String?=null,
    var sonmesajatilmatarihi:String?=null,
) {
}