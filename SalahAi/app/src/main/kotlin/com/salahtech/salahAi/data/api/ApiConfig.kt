package com.salahtech.salahAi.data.api

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.squareup.okhttp3.OkHttpClient
import com.squareup.okhttp3.logging.HttpLoggingInterceptor
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://aichatbot-atctaywz.manus.space"
private const val SESSION_COOKIE = "app_session_id"

val Context.dataStore by preferencesDataStore(name = "salahAi_settings")

object ApiConfig {
    
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun createApiService(context: Context, sessionCookie: String? = null): ChatApiService {
        val client = createOkHttpClient(context, sessionCookie)
        
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ChatApiService::class.java)
    }

    private fun createOkHttpClient(context: Context, sessionCookie: String?): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val cookieInterceptor = { chain: okhttp3.Interceptor.Chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
            
            if (sessionCookie != null) {
                requestBuilder.addHeader("Cookie", "$SESSION_COOKIE=$sessionCookie")
            }
            
            chain.proceed(requestBuilder.build())
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(okhttp3.Interceptor(cookieInterceptor))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}
