package com.salahtech.salahAi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salahtech.salahAi.data.db.ConversationEntity
import com.salahtech.salahAi.data.db.MessageEntity
import com.salahtech.salahAi.data.repository.ChatRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatUiState(
    val conversations: List<ConversationEntity> = emptyList(),
    val currentConversationId: Int? = null,
    val messages: List<MessageEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isTyping: Boolean = false,
    val error: String? = null,
    val userMessage: String = "",
    val customInstructions: String = "",
    val memoryItems: List<Pair<String, String>> = emptyList()
)

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _conversations = repository.getAllConversations()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            _conversations.collect { conversations ->
                _uiState.update { it.copy(conversations = conversations) }
            }
        }
    }

    fun createNewConversation() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                val conversation = repository.createConversation()
                _uiState.update { 
                    it.copy(
                        currentConversationId = conversation.id,
                        messages = emptyList(),
                        isLoading = false
                    )
                }
                loadMessages(conversation.id)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to create conversation"
                    )
                }
            }
        }
    }

    fun selectConversation(conversationId: Int) {
        _uiState.update { it.copy(currentConversationId = conversationId) }
        loadMessages(conversationId)
    }

    fun renameConversation(conversationId: Int, newTitle: String) {
        viewModelScope.launch {
            try {
                repository.renameConversation(conversationId, newTitle)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun deleteConversation(conversationId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteConversation(conversationId)
                if (_uiState.value.currentConversationId == conversationId) {
                    _uiState.update { 
                        it.copy(
                            currentConversationId = null,
                            messages = emptyList()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    private fun loadMessages(conversationId: Int) {
        viewModelScope.launch {
            try {
                repository.loadMessagesFromApi(conversationId)
                repository.getMessages(conversationId).collect { messages ->
                    _uiState.update { it.copy(messages = messages) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun sendMessage(message: String) {
        val conversationId = _uiState.value.currentConversationId ?: return
        
        viewModelScope.launch {
            try {
                _uiState.update { 
                    it.copy(
                        isLoading = true,
                        isTyping = true,
                        userMessage = "",
                        error = null
                    )
                }
                
                // Send user message
                repository.sendMessage(conversationId, message)
                
                // Simulate AI response (in real app, this would come from API)
                _uiState.update { it.copy(isTyping = true) }
                
                // Load updated messages
                repository.loadMessagesFromApi(conversationId)
                
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        isTyping = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        isTyping = false,
                        error = e.message ?: "Failed to send message"
                    )
                }
            }
        }
    }

    fun updateUserMessage(message: String) {
        _uiState.update { it.copy(userMessage = message) }
    }

    fun updateCustomInstructions(instructions: String) {
        _uiState.update { it.copy(customInstructions = instructions) }
        
        viewModelScope.launch {
            try {
                repository.setUserPreferences(instructions)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun addMemory(key: String, value: String) {
        val conversationId = _uiState.value.currentConversationId ?: return
        
        viewModelScope.launch {
            try {
                repository.setMemory(conversationId, key, value)
                loadMemory(conversationId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    private fun loadMemory(conversationId: Int) {
        viewModelScope.launch {
            repository.getConversationMemory(conversationId).collect { memoryList ->
                _uiState.update { 
                    it.copy(memoryItems = memoryList.map { m -> m.key to m.value })
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
