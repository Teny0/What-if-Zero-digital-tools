package com.example.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ChatMessage
import com.example.data.GeminiClient
import com.example.data.MessageRole
import com.example.data.OfflineCoachFallback
import com.example.data.OfflineTechniques
import com.example.data.SavedTactic
import com.example.data.TacticsStorage
import com.example.data.TechniqueCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class OfflineCoachViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    companion object {
        const val INITIAL_GREETING = "What's your business, who's your ideal customer, and what's your biggest marketing challenge right now?"
    }

    private val geminiClient = GeminiClient()
    private val storage = TacticsStorage(application)

    private val _messages = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage(
            role = MessageRole.MODEL,
            content = INITIAL_GREETING
        )
    ))
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _savedTactics = MutableStateFlow<List<SavedTactic>>(emptyList())
    val savedTactics: StateFlow<List<SavedTactic>> = _savedTactics.asStateFlow()

    private val _selectedCategory = MutableStateFlow<TechniqueCategory?>(null)
    val selectedCategory: StateFlow<TechniqueCategory?> = _selectedCategory.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    init {
        tts = TextToSpeech(application, this)
        loadSavedTactics()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            isTtsReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    fun onInputTextChanged(text: String) {
        _inputText.value = text
    }

    fun selectCategory(category: TechniqueCategory?) {
        _selectedCategory.value = category
    }

    fun sendCurrentInput() {
        val text = _inputText.value.trim()
        if (text.isEmpty() || _isGenerating.value) return
        _inputText.value = ""
        sendMessage(text)
    }

    fun sendMessage(query: String) {
        val userMsg = ChatMessage(
            role = MessageRole.USER,
            content = query
        )
        val pendingModelMsg = ChatMessage(
            role = MessageRole.MODEL,
            content = "",
            isGenerating = true
        )

        val updatedList = _messages.value + userMsg + pendingModelMsg
        _messages.value = updatedList
        _isGenerating.value = true

        viewModelScope.launch {
            // Build conversation history for API (excluding the pending generating message)
            val historyForApi = _messages.value.filter { !it.isGenerating }
            val result = geminiClient.generateCoachResponse(historyForApi)

            _isGenerating.value = false
            if (result.isSuccess) {
                val responseText = result.getOrNull() ?: ""
                _messages.value = _messages.value.map { msg ->
                    if (msg.id == pendingModelMsg.id) {
                        msg.copy(content = responseText, isGenerating = false)
                    } else msg
                }
            } else {
                val exception = result.exceptionOrNull()
                val errorMsg = exception?.message ?: "Unable to connect."
                Log.w("OfflineCoachViewModel", "API failed: $errorMsg, supplying offline response")

                // Provide intelligent fallback so the user always receives practical coach guidance
                val offlineGuidance = OfflineCoachFallback.generateOfflineGuidance(query)
                val combinedContent = buildString {
                    append(offlineGuidance)
                    if (errorMsg.contains("API key", ignoreCase = true)) {
                        append("\n\n---\n*💡 Note: Running in local offline playbook mode. Add your GEMINI_API_KEY in AI Studio to enable personalized real-time AI generations.*")
                    }
                }

                _messages.value = _messages.value.map { msg ->
                    if (msg.id == pendingModelMsg.id) {
                        msg.copy(content = combinedContent, isGenerating = false)
                    } else msg
                }
            }
        }
    }

    fun applyQuickTweak(promptTweak: String) {
        sendMessage(promptTweak)
    }

    fun restartConversation() {
        stopSpeech()
        _messages.value = listOf(
            ChatMessage(
                role = MessageRole.MODEL,
                content = INITIAL_GREETING
            )
        )
        _inputText.value = ""
    }

    fun toggleBookmark(message: ChatMessage) {
        val isNowBookmarked = !message.isBookmarked
        _messages.value = _messages.value.map {
            if (it.id == message.id) it.copy(isBookmarked = isNowBookmarked) else it
        }

        if (isNowBookmarked) {
            val title = message.content.lines().firstOrNull { it.isNotBlank() }
                ?.take(40)?.replace("#", "")?.trim() ?: "Marketing Script"
            val tactic = SavedTactic(
                id = message.id,
                title = title,
                categoryName = detectCategory(message.content),
                content = message.content
            )
            storage.saveTactic(tactic)
        } else {
            storage.removeTactic(message.id)
        }
        loadSavedTactics()
    }

    fun saveScriptDirectly(title: String, content: String, categoryName: String) {
        val tactic = SavedTactic(
            title = title,
            categoryName = categoryName,
            content = content
        )
        storage.saveTactic(tactic)
        loadSavedTactics()
    }

    fun deleteSavedTactic(id: String) {
        storage.removeTactic(id)
        loadSavedTactics()
    }

    private fun loadSavedTactics() {
        _savedTactics.value = storage.getSavedTactics()
    }

    private fun detectCategory(content: String): String {
        for (cat in OfflineTechniques.categories) {
            if (content.contains(cat.title, ignoreCase = true) ||
                content.contains(cat.emoji)) {
                return cat.title
            }
        }
        return "Offline Strategy"
    }

    fun speakScript(text: String) {
        if (!isTtsReady || tts == null) return

        if (_isSpeaking.value) {
            stopSpeech()
            return
        }

        // Clean markdown symbols for natural speech
        val speechText = text
            .replace("#", "")
            .replace("*", "")
            .replace(">", "")
            .replace("-", "")
            .trim()

        _isSpeaking.value = true
        tts?.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "OFFLINE_COACH_SPEECH")
    }

    fun stopSpeech() {
        if (_isSpeaking.value) {
            tts?.stop()
            _isSpeaking.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
