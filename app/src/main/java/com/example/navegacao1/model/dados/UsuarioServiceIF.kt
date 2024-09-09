package com.example.navegacao1.model.dados

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UsuarioServiceIF {

    @GET("usuarios")
    suspend fun listar(): List<Usuario>

    @GET("58013240/json/")
    suspend fun getEndereco(): Endereco

    @GET("/usuarios")
    suspend fun getUsuarios(): List<Usuario>

    @POST("/usuarios")
    suspend fun inserirUsuario(@Body usuario: Usuario)

    @DELETE("/usuarios/{id}")
    suspend fun removerUsuario(@Path("id") id: String)

    @GET("/usuarios/{id}")
    suspend fun buscarPorId(@Path("id") id: String): Usuario


}