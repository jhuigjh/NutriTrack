package com.fit2081.Gan35090251.nutritrack.data.network

import com.fit2081.Gan35090251.nutritrack.data.entity.FruitInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface FruityViceApiService {
    @GET("api/fruit/{name}")
    suspend fun getFruitInfoByName(@Path("name") name: String): FruitInfo
}