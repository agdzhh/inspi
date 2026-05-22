package com.inspi.app.data.repository

import com.inspi.app.data.local.dao.*
import com.inspi.app.data.local.entities.*
import com.inspi.app.data.preferences.InspiPreferences
import com.inspi.app.domain.models.*
import com.inspi.app.utils.StreakManager
import com.inspi.app.utils.TaskSelector
import com.inspi.app.utils.XpCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

// ── FriendRepository ──────────────────────────────────────────────────────────
@Singleton
class FriendRepository @Inject constructor(
    private val dao: FriendDao,
    private val prefs: InspiPreferences,
) {
    fun observeFriends(): Flow<List<Friend>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun getMyCode(): String = prefs.getOrCreateUserCode()

    suspend fun addFriend(code: String, username: String): Result<Friend> {
        val trimmedCode = code.trim().uppercase()
        val trimmedName = username.trim()
        if (trimmedCode.length != 6) return Result.failure(Exception("Code must be 6 characters"))
        if (dao.findByCode(trimmedCode) != null) return Result.failure(Exception("Already added"))
        val myCode = prefs.getOrCreateUserCode()
        if (trimmedCode == myCode) return Result.failure(Exception("That's your own code!"))

        // ── Magic demo code — no username required, auto-fills a preset friend ──
        if (trimmedCode == DEMO_FRIEND_CODE) {
            val entity = DEMO_FRIEND_ENTITY
            dao.insert(entity)
            return Result.success(entity.toDomain())
        }

        if (trimmedName.isBlank()) return Result.failure(Exception("Enter a username"))
        val entity = FriendEntity(code = trimmedCode, username = trimmedName, hobby = HobbyType.PHOTOGRAPHY.name)
        dao.insert(entity)
        return Result.success(entity.toDomain())
    }

    companion object {
        /** Type this code in the Add Friend form to demo the flow without a real friend. */
        const val DEMO_FRIEND_CODE = "INSPI1"
        val DEMO_FRIEND_ENTITY = FriendEntity(
            code          = DEMO_FRIEND_CODE,
            username      = "Sam 🌟",
            hobby         = HobbyType.DRAWING.name,
            weeklyXp      = 145,
            currentStreak = 9,
            avatarUrl     = "https://i.pravatar.cc/150?img=8",
        )
    }

    suspend fun removeFriend(code: String) = dao.deleteByCode(code)

    /**
     * Inserts fake friends so the leaderboard is non-empty on first launch.
     * Safe to call repeatedly — skips codes that already exist.
     */
    suspend fun seedDemoFriendsIfEmpty() {
        if (dao.observeAll().map { it.size }.first() > 0) return   // already seeded

        val demoFriends = listOf(
            FriendEntity(code = "ALEX01", username = "Alex 🌱",  hobby = HobbyType.DRAWING.name,      weeklyXp = 320, currentStreak = 12, avatarUrl = "https://i.pravatar.cc/150?img=12"),
            FriendEntity(code = "MAYA02", username = "Maya ✨",   hobby = HobbyType.PHOTOGRAPHY.name,  weeklyXp = 210, currentStreak = 7,  avatarUrl = "https://i.pravatar.cc/150?img=5"),
            FriendEntity(code = "JAKE03", username = "Jake 🏃",  hobby = HobbyType.DRAWING.name,      weeklyXp = 175, currentStreak = 5,  avatarUrl = "https://i.pravatar.cc/150?img=33"),
            FriendEntity(code = "NINA04", username = "Nina 🎨",  hobby = HobbyType.PHOTOGRAPHY.name,  weeklyXp = 90,  currentStreak = 3,  avatarUrl = "https://i.pravatar.cc/150?img=47"),
        )
        demoFriends.forEach { friend ->
            if (dao.findByCode(friend.code) == null) dao.insert(friend)
        }
    }
}

// ── UserRepository ────────────────────────────────────────────────────────────
@Singleton
class UserRepository @Inject constructor(
    private val dao: UserProfileDao,
    private val prefs: InspiPreferences,
) {
    fun observeProfile(): Flow<UserProfile?> = dao.observeProfile().map { it?.toDomain() }

    suspend fun getProfile(): UserProfile? = dao.getProfile()?.toDomain()

    suspend fun ensureProfileExists() {
        if (dao.getProfile() == null) {
            dao.upsertProfile(UserProfileEntity())
        }
    }

    suspend fun updateProfile(profile: UserProfile) {
        dao.upsertProfile(profile.toEntity())
    }

    /**
     * Called on every app open.
     * Evaluates streak health and applies penalty if a day was missed.
     * Returns true if the streak was broken (so UI can show a toast).
     */
    suspend fun evaluateAndUpdateStreak(): Boolean {
        val entity = dao.getProfile() ?: return false
        val update = StreakManager.evaluateStreak(
            currentStreak = entity.currentStreak,
            longestStreak = entity.longestStreak,
            currentXp = entity.totalXp,
            lastSubmissionDate = entity.lastSubmissionDate,
        )
        if (update.streakBroken || update.newStreak != entity.currentStreak || update.newXp != entity.totalXp) {
            dao.upsertProfile(
                entity.copy(
                    currentStreak = update.newStreak,
                    totalXp = update.newXp,
                )
            )
        }
        return update.streakBroken
    }

    /**
     * Called after a successful task submission.
     */
    suspend fun recordTaskCompletion(xpEarned: Int) {
        val entity = dao.getProfile() ?: return
        val alreadyDoneToday = StreakManager.completedToday(entity.lastSubmissionDate)
        if (alreadyDoneToday) return // first completion only counts

        val newStreak = entity.currentStreak + 1
        val newLongest = maxOf(entity.longestStreak, newStreak)
        dao.upsertProfile(
            entity.copy(
                currentStreak = newStreak,
                longestStreak = newLongest,
                totalXp = entity.totalXp + xpEarned,
                lastSubmissionDate = System.currentTimeMillis(),
            )
        )
    }

    fun observeHobby(): Flow<String?> = prefs.hobby
    suspend fun setHobby(hobby: String) {
        prefs.setHobby(hobby)
        // Read the username saved by NicknameViewModel (DataStore only) so it
        // is written into Room alongside the hobby — without this the entity
        // falls back to the "Creative" default and the nickname is lost.
        val savedUsername = prefs.username.first()
        val entity = dao.getProfile() ?: UserProfileEntity()
        // NOTE: We intentionally do NOT reset currentStreak or totalXp here.
        // The streak belongs to the user's overall habit, not a specific hobby.
        dao.upsertProfile(
            entity.copy(
                hobby = hobby,
                username = savedUsername ?: entity.username,
            )
        )
    }

    fun observeNotifications(): Flow<Boolean> = prefs.notificationsOn
    suspend fun setNotificationsOn(on: Boolean) = prefs.setNotificationsOn(on)
}

// ── SubmissionRepository ──────────────────────────────────────────────────────
@Singleton
class SubmissionRepository @Inject constructor(
    private val dao: SubmissionDao,
) {
    fun observeAll(): Flow<List<Submission>> = dao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun getById(id: Long): Submission? = dao.getById(id)?.toDomain()

    suspend fun insert(submission: Submission): Long = dao.insert(submission.toEntity())

    suspend fun getFlashbackPair(): Pair<Submission, Submission>? {
        val latest = dao.getLatest()?.toDomain() ?: return null
        val past = dao.getRandomPastSubmission()?.toDomain() ?: return null
        return past to latest
    }

    suspend fun count(): Int = dao.count()

    suspend fun getRetakeCandidate(hobby: HobbyType): Submission? {
        val cutoff = System.currentTimeMillis() - 21L * 24 * 60 * 60 * 1000
        return dao.getRetakeCandidate(cutoff, hobby.name)?.toDomain()
    }

    suspend fun getWeeklySubmissions(): List<Submission> {
        val weekStart = StreakManager.currentWeekStart()
        return dao.getSubmissionsSince(weekStart).map { it.toDomain() }
    }
}

// ── ChallengeRepository ───────────────────────────────────────────────────────
@Singleton
class ChallengeRepository @Inject constructor(
    private val dao: ChallengeDao,
) {
    fun observeCurrentChallenge(hobby: HobbyType): Flow<WeeklyChallenge?> {
        val weekStart = StreakManager.currentWeekStart()
        return dao.observeCurrentChallenge(hobby.name, weekStart).map { it?.toDomain() }
    }

    suspend fun ensureCurrentChallengeExists(hobby: HobbyType) {
        val weekStart = StreakManager.currentWeekStart()
        // Check via direct query not via flow
        val (title, description) = TaskSelector.getCurrentChallenge(hobby)
        dao.upsert(
            ChallengeEntity(
                hobbyType = hobby.name,
                title = title,
                description = description,
                weekStartDate = weekStart,
            )
        )
    }

    suspend fun markCompleted(id: Long) = dao.markCompleted(id)
}

// ── CoachRepository ───────────────────────────────────────────────────────────
@Singleton
class CoachRepository @Inject constructor(
    private val dao: CoachMessageDao,
) {
    fun observeMessages(): Flow<List<CoachMessage>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun getHistory(): List<CoachMessage> =
        dao.getLastN(20).map { it.toDomain() }.reversed()

    suspend fun insertMessage(message: CoachMessage) {
        dao.insert(message.toEntity())
    }

    suspend fun clearAll() = dao.clearAll()
}

// ── Mappers ───────────────────────────────────────────────────────────────────
private fun UserProfileEntity.toDomain() = UserProfile(
    username = username,
    hobby = HobbyType.fromString(hobby),
    totalXp = totalXp,
    currentStreak = currentStreak,
    longestStreak = longestStreak,
    lastSubmissionDate = lastSubmissionDate,
)

private fun UserProfile.toEntity() = UserProfileEntity(
    id = 1,
    username = username,
    hobby = hobby.name,
    totalXp = totalXp,
    currentStreak = currentStreak,
    longestStreak = longestStreak,
    lastSubmissionDate = lastSubmissionDate,
)

private fun SubmissionEntity.toDomain() = Submission(
    id = id,
    imagePath = imagePath,
    thumbnailPath = thumbnailPath,
    taskTitle = taskTitle,
    createdAt = createdAt,
    hobbyType = HobbyType.fromString(hobbyType),
    xpEarned = xpEarned,
)

private fun Submission.toEntity() = SubmissionEntity(
    id = id,
    imagePath = imagePath,
    thumbnailPath = thumbnailPath,
    taskTitle = taskTitle,
    createdAt = createdAt,
    hobbyType = hobbyType.name,
    xpEarned = xpEarned,
)

private fun ChallengeEntity.toDomain(): WeeklyChallenge {
    val millisLeft = weekStartDate + TimeUnit.DAYS.toMillis(7) - System.currentTimeMillis()
    val daysLeft = maxOf(0, (millisLeft / TimeUnit.DAYS.toMillis(1)).toInt())
    return WeeklyChallenge(
        id = id,
        title = title,
        description = description,
        daysRemaining = daysLeft,
        isCompleted = isCompleted,
    )
}

private fun CoachMessageEntity.toDomain() = CoachMessage(
    id = id,
    role = if (role == "user") MessageRole.USER else MessageRole.ASSISTANT,
    content = content,
    createdAt = createdAt,
)

private fun CoachMessage.toEntity() = CoachMessageEntity(
    id = id,
    role = if (role == MessageRole.USER) "user" else "assistant",
    content = content,
    createdAt = createdAt,
)

private fun FriendEntity.toDomain() = Friend(
    code = code,
    username = username,
    hobby = HobbyType.fromString(hobby),
    weeklyXp = weeklyXp,
    currentStreak = currentStreak,
    avatarUrl = avatarUrl,
)