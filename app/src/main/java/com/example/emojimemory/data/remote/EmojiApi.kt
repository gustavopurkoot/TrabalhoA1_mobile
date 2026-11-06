package com.example.emojimemory.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// Um emoji como a API EmojiHub devolve. Exemplo de "unicode": ["U+1F436"]
data class EmojiDto(
    val name: String = "",
    val unicode: List<String> = emptyList()
)

interface EmojiApiService {

    // Ex.: api/all/category/animals-and-nature  (API gratuita, sem chave)
    @GET("api/all/category/{categoria}")
    suspend fun buscarPorCategoria(@Path("categoria") categoria: String): List<EmojiDto>
}

// Cria o Retrofit uma única vez e reaproveita em todo o app
object EmojiApi {

    private const val BASE_URL = "https://emojihub.yurace.pro/"

    val service: EmojiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmojiApiService::class.java)
    }
}
