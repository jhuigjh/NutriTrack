package com.fit2081.Gan35090251.nutritrack.data.network


import com.fit2081.Gan35090251.nutritrack.data.entity.GeminiRequest
import com.fit2081.Gan35090251.nutritrack.data.entity.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(

        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}
