package com.salahtech.salahAi.data.repository

import com.salahtech.salahAi.data.api.*
import com.salahtech.salahAi.data.db.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepository(
    private val apiService: ChatApiService,
    private val database: SalahAiDatabase
) {
    private val conversationDao = database.conversationDao()
    private val messageDao = database.messageDao()
    private val attachmentDao = database.attachmentDao()
    private val memoryDao = database.conversationMemoryDao()
    private val preferencesDao = database.userPreferencesDao()

    // Conversations
    fun getAllConversations(): Flow<List<ConversationEntity>> {
        return conversationDao.getAllConversations()
    }

    suspend fun createConversation(title: String = "New Chat"): ConversationEntity {
        val response = apiService.createConversation(CreateConversationRequest(title))
        val entity = ConversationEntity(
            id = response.id,
            userId = response.userId,
            title = response.title,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        conversationDao.insertConversation(entity)
        return entity
    }

    suspend fun renameConversation(conversationId: Int, title: String): ConversationEntity {
        val response = apiService.renameConversation(conversationId, title)
        val entity = ConversationEntity(
            id = response.id,
            userId = response.userId,
            title = response.title,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        conversationDao.updateConversation(entity)
        return entity
    }

    suspend fun deleteConversation(conversationId: Int) {
        apiService.deleteConversation(conversationId)
        conversationDao.deleteConversationById(conversationId)
    }

    // Messages
    fun getMessages(conversationId: Int): Flow<List<MessageEntity>> {
        return messageDao.getMessages(conversationId)
    }

    suspend fun sendMessage(conversationId: Int, message: String): MessageEntity {
        val response = apiService.sendMessage(SendMessageRequest(conversationId, message))
        val entity = MessageEntity(
            id = response.id,
            conversationId = response.conversationId,
            role = response.role,
            content = response.content,
            createdAt = System.currentTimeMillis()
        )
        messageDao.insertMessage(entity)
        return entity
    }

    suspend fun loadMessagesFromApi(conversationId: Int) {
        try {
            val messages = apiService.getMessages(conversationId)
            val entities = messages.map { msg ->
                MessageEntity(
                    id = msg.id,
                    conversationId = msg.conversationId,
                    role = msg.role,
                    content = msg.content,
                    searchResults = msg.searchResults,
                    createdAt = System.currentTimeMillis()
                )
            }
            messageDao.insertMessages(entities)
        } catch (e: Exception) {
            // Handle error - messages might already be cached
        }
    }

    // Advanced Features
    suspend fun uploadFile(
        conversationId: Int,
        fileName: String,
        mimeType: String,
        fileData: ByteArray
    ): UploadFileResponse {
        return apiService.uploadFile(conversationId, fileName, mimeType, fileData)
    }

    suspend fun browseWeb(url: String): String {
        return apiService.browseWeb(url)
    }

    suspend fun executeCode(conversationId: Int, code: String): String {
        return apiService.executeCode(conversationId, code)
    }

    // Memory
    fun getConversationMemory(conversationId: Int): Flow<List<ConversationMemoryEntity>> {
        return memoryDao.getMemory(conversationId)
    }

    suspend fun setMemory(conversationId: Int, key: String, value: String): ConversationMemoryEntity {
        val response = apiService.setMemory(conversationId, key, value)
        val entity = ConversationMemoryEntity(
            id = response.id,
            conversationId = response.conversationId,
            key = response.key,
            value = response.value,
            createdAt = System.currentTimeMillis()
        )
        memoryDao.insertMemory(entity)
        return entity
    }

    // User Preferences
    suspend fun getUserPreferences(): UserPreferencesEntity? {
        return try {
            val response = apiService.getUserPreferences()
            response?.let {
                UserPreferencesEntity(
                    id = it.id,
                    userId = it.userId,
                    systemPrompt = it.systemPrompt,
                    preferences = it.preferences
                )
            }
        } catch (e: Exception) {
            preferencesDao.getPreferences()
        }
    }

    suspend fun setUserPreferences(systemPrompt: String): UserPreferencesEntity {
        val response = apiService.setUserPreferences(
            UserPreferences(
                id = 0,
                userId = 0,
                systemPrompt = systemPrompt
            )
        )
        val entity = UserPreferencesEntity(
            id = response.id,
            userId = response.userId,
            systemPrompt = response.systemPrompt,
            preferences = response.preferences
        )
        preferencesDao.insertPreferences(entity)
        return entity
    }

    // Authentication
    suspend fun getCurrentUser(): AuthUser {
        return apiService.getCurrentUser()
    }

    suspend fun logout() {
        apiService.logout()
    }
}
