package com.inspi.app.data.local.dao

import androidx.room.*
import com.inspi.app.data.local.entities.*
import kotlinx.coroutines.flow.Flow

// ── UserProfile ───────────────────────────────────────────────────────────────
@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observeProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: UserProfileEntity)
}

// ── Submissions ───────────────────────────────────────────────────────────────
@Dao
interface SubmissionDao {
    @Query("SELECT * FROM submissions ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<SubmissionEntity>>

    @Query("SELECT * FROM submissions WHERE hobbyType = :hobby ORDER BY createdAt DESC")
    fun observeByHobby(hobby: String): Flow<List<SubmissionEntity>>

    @Query("SELECT * FROM submissions WHERE id = :id")
    suspend fun getById(id: Long): SubmissionEntity?

    @Query("SELECT COUNT(*) FROM submissions")
    suspend fun count(): Int

    @Insert
    suspend fun insert(submission: SubmissionEntity): Long

    @Query("""
        SELECT * FROM submissions
        WHERE id != (SELECT id FROM submissions ORDER BY createdAt DESC LIMIT 1)
        ORDER BY RANDOM() LIMIT 1
    """)
    suspend fun getRandomPastSubmission(): SubmissionEntity?

    @Query("SELECT * FROM submissions ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatest(): SubmissionEntity?
}

// ── Challenges ────────────────────────────────────────────────────────────────
@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges WHERE hobbyType = :hobby AND weekStartDate = :weekStart LIMIT 1")
    fun observeCurrentChallenge(hobby: String, weekStart: Long): Flow<ChallengeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(challenge: ChallengeEntity)

    @Query("UPDATE challenges SET isCompleted = 1 WHERE id = :id")
    suspend fun markCompleted(id: Long)
}

// ── CoachMessages ─────────────────────────────────────────────────────────────
@Dao
interface CoachMessageDao {
    @Query("SELECT * FROM coach_messages ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<CoachMessageEntity>>

    @Query("SELECT * FROM coach_messages ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getLastN(limit: Int): List<CoachMessageEntity>

    @Insert
    suspend fun insert(message: CoachMessageEntity): Long

    @Query("DELETE FROM coach_messages")
    suspend fun clearAll()
}
