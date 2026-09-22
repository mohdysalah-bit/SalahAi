package com.salahtech.salahAi.data.api

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://aichatbot-atctaywz.manus.space/"
private const val SESSION_COOKIE = "app_session_id"

val Context.dataStore by preferencesDataStore(name = "salahAi_settings")

object ApiConfig {
    fun createApiService(context: Context, sessionCookie: String? = null): ChatApiService {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        val cookieInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder().apply {
                if (!sessionCookie.isNullOrBlank()) {
                    addHeader("Cookie", "$SESSION_COOKIE=$sessionCookie")
                }
            }.build()
            chain.proceed(request)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(cookieInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatApiService::class.java)
    }
}
