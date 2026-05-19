package com.inspi.app.network

import android.graphics.BitmapFactory
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

    override suspend fun sendImageCritique(
        imagePath: String,
        taskTitle: String,
        hobby: String,
        streak: Int,
    ): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val bitmap = BitmapFactory.decodeFile(imagePath)
                ?: throw IllegalStateException("Cannot decode image for critique")

            val prompt = """
                The user just submitted this $hobby image for the task: "$taskTitle".
                They have a $streak-day streak.

                Give feedback in this structure:
                1. One specific positive observation about what you actually see in the image
                2. One or two concrete, actionable improvement tips based on the image
                3. A brief optional follow-up challenge related to this task

                Keep the total response under 120 words. Be specific to the image — avoid generic praise.
            """.trimIndent()

            val inputContent = content {
                image(bitmap)
                text(prompt)
            }

            val response = model.generateContent(inputContent)
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

                Write a personalized 2–3 sentence weekly progress summary.
                Reference the specific task names. Be encouraging and specific — not generic.
                No bullet points. Keep it warm and conversational.
            """.trimIndent()

            val response = model.generateContent(prompt)
            response.text ?: throw IllegalStateException("Empty weekly insight response")
        }.onFailure { err ->
            Log.e("GeminiCoachService", "Gemini weekly insight error", err)
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
            "Nice work on \"$taskTitle\"! The subject placement feels intentional and the framing is balanced. " +
            "Next time, try adjusting your exposure slightly to preserve highlight detail in brighter areas. " +
            "Follow-up challenge: try the same composition from a lower angle and see how it changes the mood."
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
