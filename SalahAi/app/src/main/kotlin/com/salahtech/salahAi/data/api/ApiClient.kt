package com.salahtech.salahAi.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.*

// Data Models
@Serializable
data class Message(
    val id: Int,
    val conversationId: Int,
    val role: String, // "user" or "assistant"
    val content: String,
    val searchResults: String? = null,
    val createdAt: String
)

@Serializable
data class Conversation(
    val id: Int,
    val userId: Int,
    val title: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class SendMessageRequest(
    val conversationId: Int,
    val message: String,
    val includeWebSearch: Boolean = true
)

@Serializable
data class SendMessageResponse(
    val id: Int,
    val conversationId: Int,
    val role: String,
    val content: String,
    val createdAt: String
)

@Serializable
data class CreateConversationRequest(
    val title: String = "New Chat"
)

@Serializable
data class CreateConversationResponse(
    val id: Int,
    val userId: Int,
    val title: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class UserPreferences(
    val id: Int,
    val userId: Int,
    val systemPrompt: String,
    val preferences: String? = null
)

@Serializable
data class ConversationMemory(
    val id: Int,
    val conversationId: Int,
    val key: String,
    val value: String,
    val createdAt: String
)

@Serializable
data class UploadFileResponse(
    val key: String,
    val url: String,
    val fileName: String,
    val mimeType: String,
    val size: Long
)

@Serializable
data class AuthUser(
    val id: Int,
    val openId: String,
    val name: String? = null,
    val email: String? = null,
    val role: String = "user",
    val createdAt: String,
    val updatedAt: String,
    val lastSignedIn: String
)

// API Service Interface
interface ChatApiService {
    
    // Authentication
    @GET("/api/trpc/auth.me")
    suspend fun getCurrentUser(): AuthUser

    @POST("/api/trpc/auth.logout")
    suspend fun logout()

    // Conversations
    @GET("/api/trpc/chat.listConversations")
    suspend fun listConversations(): List<Conversation>

    @POST("/api/trpc/chat.createConversation")
    suspend fun createConversation(@Body request: CreateConversationRequest): CreateConversationResponse

    @GET("/api/trpc/chat.getConversation")
    suspend fun getConversation(@Query("conversationId") conversationId: Int): Conversation

    @POST("/api/trpc/chat.renameConversation")
    suspend fun renameConversation(
        @Query("conversationId") conversationId: Int,
        @Query("title") title: String
    ): Conversation

    @POST("/api/trpc/chat.deleteConversation")
    suspend fun deleteConversation(@Query("conversationId") conversationId: Int)

    // Messages
    @GET("/api/trpc/chat.getMessages")
    suspend fun getMessages(@Query("conversationId") conversationId: Int): List<Message>

    @POST("/api/trpc/chat.sendMessage")
    suspend fun sendMessage(@Body request: SendMessageRequest): SendMessageResponse

    // Advanced Features
    @POST("/api/trpc/advanced.uploadFile")
    suspend fun uploadFile(
        @Query("conversationId") conversationId: Int,
        @Query("fileName") fileName: String,
        @Query("mimeType") mimeType: String,
        @Body fileData: ByteArray
    ): UploadFileResponse

    @POST("/api/trpc/advanced.browseWeb")
    suspend fun browseWeb(@Query("url") url: String): String

    @POST("/api/trpc/advanced.executeCode")
    suspend fun executeCode(
        @Query("conversationId") conversationId: Int,
        @Query("code") code: String
    ): String

    @POST("/api/trpc/advanced.setMemory")
    suspend fun setMemory(
        @Query("conversationId") conversationId: Int,
        @Query("key") key: String,
        @Query("value") value: String
    ): ConversationMemory

    @GET("/api/trpc/advanced.getMemory")
    suspend fun getMemory(@Query("conversationId") conversationId: Int): List<ConversationMemory>

    @POST("/api/trpc/advanced.setUserPreferences")
    suspend fun setUserPreferences(@Body preferences: UserPreferences): UserPreferences

    @GET("/api/trpc/advanced.getUserPreferences")
    suspend fun getUserPreferences(): UserPreferences?

    @POST("/api/trpc/advanced.sendAdvancedMessage")
    suspend fun sendAdvancedMessage(
        @Query("conversationId") conversationId: Int,
        @Query("message") message: String,
        @Query("includeWebSearch") includeWebSearch: Boolean = true,
        @Query("includeCodeExecution") includeCodeExecution: Boolean = false
    ): SendMessageResponse
}
