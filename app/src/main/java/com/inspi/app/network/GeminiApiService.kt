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
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException

interface CoachApiService {
    suspend fun sendMessage(
        hobby: String,
        currentTask: String,
        streak: Int,
        history: List<CoachMessage>,
        userMessage: String,
    ): Result<String>

    suspend fun sendImageCritique(
        imagePath: String,
        taskTitle: String,
        hobby: String,
        streak: Int,
    ): Result<String>

    suspend fun generateWeeklyInsight(
        hobby: String,
        taskTitles: List<String>,
        streak: Int,
    ): Result<String>
}

// ── System instruction ────────────────────────────────────────────────────────
private const val SYSTEM_INSTRUCTION = """
You are Inspi Coach, a friendly and knowledgeable creative mentor inside the Inspi app.
IMPORTANT: Do NOT use markdown formatting like **bold** or *italic*.
Write in plain text only. Use numbers for lists (1. 2. 3.).
Keep responses concise and conversational.
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

// ── Real Gemini implementation ────────────────────────────────────────────────
@Singleton
class GeminiCoachService @Inject constructor(
    private val apiKey: String,
) : CoachApiService {

    init {
        Log.d("GEMINI_TEST", "KEY LENGTH = ${apiKey.length}")
    }

    private val model by lazy {
        GenerativeModel(
            "gemini-2.5-flash",
            apiKey = apiKey,
            systemInstruction = content { text(SYSTEM_INSTRUCTION) },
            generationConfig = generationConfig {
                temperature = 0.8f
                maxOutputTokens = 2048
            }
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
            val geminiHistory = history
                .takeLast(6)
                .filter { it.content.isNotBlank() }
                .map { msg ->
                    content(role = if (msg.role == MessageRole.USER) "user" else "model") {
                        text(msg.content)
                    }
                }

            val chat = model.startChat(history = geminiHistory)
            val contextPrefix = "[Hobby: $hobby | Task: \"$currentTask\" | Streak: $streak days]\n"

            Log.d("GEMINI_TEST", "Sending request...")
            val response = withTimeout(60000) {
                chat.sendMessage(contextPrefix + userMessage)
            }
            Log.d("GEMINI_TEST", "Response received")

            response.text ?: throw IllegalStateException("Empty response from Gemini")
        }.onFailure { err ->
            when (err) {
                is TimeoutCancellationException -> Log.e("GeminiCoachService", "Gemini timeout", err)
                else -> Log.e("GeminiCoachService", "Gemini API error", err)
            }
        }
    }

    override suspend fun sendImageCritique(
        imagePath: String,
        taskTitle: String,
        hobby: String,
        streak: Int,
    ): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val prompt = """
                [Hobby: $hobby | Task: "$taskTitle" | Streak: $streak days]
                The user just completed the task "$taskTitle".

                React naturally to their accomplishment — like a coach who just saw them finish.
                1. Acknowledge completing this specific task with genuine enthusiasm
                2. Give one or two concrete tips relevant to "$taskTitle"
                3. Optional: a small follow-up challenge to push them further

                Keep it under 100 words. Sound like a real person, not a checklist.
            """.trimIndent()

            Log.d("GEMINI_TEST", "Sending critique request...")
            val response = model.generateContent(prompt)
            Log.d("GEMINI_TEST", "Critique received")

            response.text ?: throw IllegalStateException("Empty critique response from Gemini")
        }.onFailure { err ->
            Log.e("GeminiCoachService", "Gemini critique error", err)
        }
    }

    override suspend fun generateWeeklyInsight(
        hobby: String,
        taskTitles: List<String>,
        streak: Int,
    ): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val taskList = taskTitles.joinToString(", ")
            val prompt = """
                A user is working on $hobby. This week they completed ${taskTitles.size} task(s): $taskList.
                Their current streak is $streak days.

                Write a personalized 2-3 sentence weekly progress summary.
                Reference the specific task names. Be encouraging and specific, not generic.
                No bullet points. Keep it warm and conversational.
            """.trimIndent()

            val response = model.generateContent(prompt)
            response.text ?: throw IllegalStateException("Empty weekly insight response")
        }.onFailure { err ->
            Log.e("GeminiCoachService", "Gemini weekly insight error", err)
        }
    }
}

// ── Fake implementation ───────────────────────────────────────────────────────
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
                    "Your $streak-day streak shows real commitment. Keep it up!"
        )
    }

    override suspend fun sendImageCritique(
        imagePath: String,
        taskTitle: String,
        hobby: String,
        streak: Int,
    ): Result<String> {
        kotlinx.coroutines.delay(1500)
        return Result.success(
            "Nice work finishing \"$taskTitle\"! Really solid effort. " +
                    "Next time, pay attention to your light source — it can make or break this kind of shot. " +
                    "Follow-up challenge: try the same task again but change your angle completely."
        )
    }

    override suspend fun generateWeeklyInsight(
        hobby: String,
        taskTitles: List<String>,
        streak: Int,
    ): Result<String> {
        kotlinx.coroutines.delay(800)
        return Result.success(
            "Great week! You completed ${taskTitles.size} $hobby task(s) including ${taskTitles.firstOrNull() ?: "some great work"}. " +
                    "Your $streak-day streak shows real consistency — that's where improvement actually happens."
        )
    }
}