package com.example.navegacao1.model.dados

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
//    private const val BASE_URL = "http://viacep.com.br/ws/"
    private const val BASE_URL = "http://192.168.1.55:3000/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val usuarioService: UsuarioServiceIF = retrofit.create(UsuarioServiceIF::class.java)

}