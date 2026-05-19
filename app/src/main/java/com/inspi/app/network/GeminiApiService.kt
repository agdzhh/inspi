package com.inspi.app.network

import android.util.Log
import com.inspi.app.domain.models.CoachMessage
import com.inspi.app.domain.models.MessageRole
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

// ── Contract (same interface as before — ViewModel doesn't change) ─────────────
interface CoachApiService {
    suspend fun sendMessage(
        hobby: String,
        currentTask: String,
        streak: Int,
        history: List<CoachMessage>,
        userMessage: String,
    ): Result<String>
}

// ── System instruction shared between real and fake ───────────────────────────
private const val SYSTEM_INSTRUCTION = """
You are Inspi Coach, a friendly and knowledgeable creative mentor inside the Inspi app.
Your job is to help users improve at their chosen hobby — either photography or drawing.
Your tone is warm, casual, and encouraging — like a talented friend who happens to know a lot.
Your advice should always be practical, specific, and genuinely useful.
When giving tips:
  - Keep them short and actionable
  - Avoid generic motivational filler
  - Tailor advice to the user's hobby and their current task if provided
  - Occasionally reference their streak or progress to keep them motivated
Never be condescending. Never over-compliment. Be honest and helpful.
"""

// ── Real Gemini implementation ─────────────────────────────────────────────────
@Singleton
class GeminiCoachService @Inject constructor(
    private val apiKey: String,
) : CoachApiService {

    // Lazily create the model so we don't crash at inject-time if the key is empty
    private val model by lazy {
        GenerativeModel(
            modelName = "gemini-2.5-flash", // updated: 2.0-flash deprecated March 2026
            apiKey = apiKey,
            generationConfig = generationConfig {
                temperature = 0.8f
                maxOutputTokens = 512
            },
            systemInstruction = content { text(SYSTEM_INSTRUCTION) },
        )
    }

    override suspend fun sendMessage(
        hobby: String,
        currentTask: String,
        streak: Int,
        history: List<CoachMessage>,
        userMessage: String,
    ): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            // Build Gemini chat history from last 10 messages (5 exchange pairs)
            val geminiHistory = history.takeLast(10).map { msg ->
                content(role = if (msg.role == MessageRole.USER) "user" else "model") {
                    text(msg.content)
                }
            }

            val chat = model.startChat(history = geminiHistory)

            // Prepend context to the user's message
            val contextPrefix = "[Hobby: $hobby | Task: \"$currentTask\" | Streak: $streak days]\n"
            val response = chat.sendMessage(contextPrefix + userMessage)

            response.text ?: throw IllegalStateException("Empty response from Gemini")
        }.onFailure { err ->
            Log.e("GeminiCoachService", "Gemini API error", err)
        }
    }
}

// ── Fake implementation for local testing (no network) ────────────────────────
@Singleton
class FakeCoachService @Inject constructor() : CoachApiService {
    override suspend fun sendMessage(
        hobby: String,
        currentTask: String,
        streak: Int,
        history: List<CoachMessage>,
        userMessage: String,
    ): Result<String> {
        kotlinx.coroutines.delay(700)
        return Result.success(
            "Great question! For $hobby, focus on light first — everything else follows. " +
            "Your $streak-day streak shows real commitment. Keep it up! 💪"
        )
    }
}
