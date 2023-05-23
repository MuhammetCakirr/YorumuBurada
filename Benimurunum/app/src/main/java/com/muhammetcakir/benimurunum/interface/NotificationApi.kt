package com.muhammetcakir.benimurunum.`interface`

import com.muhammetcakir.benimurunum.Constans.Constans.Companion.CONTENT_TYPE
import com.muhammetcakir.benimurunum.Constans.Constans.Companion.SERVER_KEY
import com.muhammetcakir.benimurunum.Models.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}