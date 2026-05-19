package com.inspi.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inspi.app.data.local.dao.*
import com.inspi.app.data.local.entities.*

@Database(
    entities = [
        UserProfileEntity::class,
        SubmissionEntity::class,
        ChallengeEntity::class,
        CoachMessageEntity::class,
        FriendEntity::class,
    ],
    version = 2,
    exportSchema = false
)
abstract class InspiDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun submissionDao(): SubmissionDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun coachMessageDao(): CoachMessageDao
    abstract fun friendDao(): FriendDao
}
