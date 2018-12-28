package com.troy.tersive.model.ws

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TersiveApi {

    @PUT("user")
    fun userRegister(
        @Body user: UserDto
    ): Call<ServerResponse>

    @POST("user")
    fun userUpdate(
        @Body user: UserDto
    ): Call<ServerResponse>

    @DELETE("user")
    fun userDelete(
        @Body user: UserDto
    ): Call<ServerResponse>

    @GET("user")
    fun userLogin(
        @Query("email") email: String,
        @Query("pass") passHash: Long
    ): Call<ServerResponse>
}
